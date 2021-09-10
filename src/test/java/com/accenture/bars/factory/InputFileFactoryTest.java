package com.accenture.bars.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.accenture.bars.domain.Request;
import com.accenture.bars.exception.BarsException;
import com.accenture.bars.file.AbstractInputFile;
import com.accenture.bars.file.CSVInputFileImpl;
import com.accenture.bars.file.TextInputFileImpl;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class InputFileFactoryTest {

    @Test
    public void testGetInstance(){
        InputFileFactory factory = InputFileFactory.getInstance();
        assertTrue(factory instanceof InputFileFactory);
    }

    @Test
    public void testGetInputFileText() throws BarsException {
        InputFileFactory factory = InputFileFactory.getInstance();
        File file   = new File("C:\\BARS_TEST\\valid-txt.txt");
        AbstractInputFile txtInputFile = factory.getInputFile(file);
        assertTrue(txtInputFile instanceof TextInputFileImpl);
    }

    @Test
    public void testGetInputFileCSV() throws BarsException {
        InputFileFactory factory = InputFileFactory.getInstance();
        File file = new File("C:\\BARS_TEST\\valid-csv.csv");
        AbstractInputFile txtInputFile = factory.getInputFile(file);
        assertTrue(txtInputFile instanceof CSVInputFileImpl);
    }

    @Test
    public void testFileNotSupported() throws BarsException {
        String filename = "unsupported-file.png";
        String pathLocation = "C:\\BARS_TEST\\" + filename;
        File file = new File(pathLocation);
        InputFileFactory factory = InputFileFactory.getInstance();

        try {
            AbstractInputFile abstractInputFile =
                    factory.getInputFile(file);

            abstractInputFile.setFile(file);
            List<Request> requests = abstractInputFile.readFile();
        } catch (ResponseStatusException message) {
            String expectedMessage = BarsException.FILE_NOT_SUPPORTED;
            String actualMessage = message.getReason();
            assertEquals(expectedMessage,actualMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

