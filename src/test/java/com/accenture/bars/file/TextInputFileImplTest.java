package com.accenture.bars.file;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.accenture.bars.domain.Request;
import com.accenture.bars.exception.BarsException;
import com.accenture.bars.factory.InputFileFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TextInputFileImplTest {

    @Test
    void testValidRequestParameter() throws Exception{
        String filename = "valid-txt.txt";
        String pathLocation = "C:\\BARS_TEST\\" + filename;
        File file = new File(pathLocation);
        InputFileFactory factory = InputFileFactory.getInstance();
        AbstractInputFile abstractInputFile = factory.getInputFile(file);

        abstractInputFile.setFile(file);
        List<Request> actual = abstractInputFile.readFile();
        List<Request> expected = new ArrayList<>();
        expected.add(new Request(1, LocalDate.of(2013,
                01, 15), LocalDate.of(2013, 02, 14)));
        expected.add(new Request(1, LocalDate.of(2016,
                01, 15), LocalDate.of(2016, 02, 14)));
        Assertions.assertTrue(actual.equals(expected));
    }

    @Test
    void testInvalidRequestWithInvalidBillingCycle()  throws Exception{
        String filename = "billing-cycle-not-on-range-txt.txt";
        String pathLocation = "C:\\BARS_TEST\\" + filename;
        File file = new File(pathLocation);
        InputFileFactory factory = InputFileFactory.getInstance();

        try {
            factory.getInputFile(file);
        } catch (BarsException message){
            String expectedMessage = BarsException.BILLING_CYCLE_NOT_ON_RANGE + " " + 3;
            String actualMessage = message.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));
        }
    }

    @Test
    void testInvalidRequestWithInvalidStartDate() throws Exception{
        String filename = "invalid-start-date-txt.txt";
        String pathLocation = "C:\\BARS_TEST\\" + filename;
        File file  = new File(pathLocation);
        InputFileFactory factory= InputFileFactory.getInstance();

        try{
             factory.getInputFile(file);
        }catch (BarsException message)  {
            String expectedMessage = BarsException.INVALID_START_DATE_FORMAT;
            String actualMessage = message.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));
        }
    }

    @Test
    void testInvalidRequestWithInvalidEndDate() throws Exception{
        String filename = "invalid-end-date-txt.txt";
        String pathLocation = "C:\\BARS_TEST\\" + filename;
        File file  = new File(pathLocation);
        InputFileFactory factory= InputFileFactory.getInstance();

        try{
            factory.getInputFile(file);
        }catch (BarsException message)  {
            String expectedMessage = BarsException.INVALID_END_DATE_FORMAT;
            String actualMessage = message.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));
        }
    }
}
