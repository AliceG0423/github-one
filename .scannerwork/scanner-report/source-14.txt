package com.accenture.bars.file;


import com.accenture.bars.domain.Request;
import com.accenture.bars.exception.BarsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class TextInputFileImpl extends  AbstractInputFile{

    private static final Logger log =
            LoggerFactory.getLogger(TextInputFileImpl.class);



    public TextInputFileImpl() {
        //this is an empty constructor
    }

    @Override
    public List<Request> readFile() throws IOException, BarsException {

        List<Request> requests = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");


        LocalDate startDate;
        LocalDate endDate;
        FileReader fileReader;
        int row = 1;
        final int B_CYCLE_INDEX = 2;
        final int START_DATE_INDEX_A = 2;
        final int START_DATE_INDEX_B = 10;
        final int END_DATE_INDEX_A = 10;
        final int END_DATE_INDEX_B = 18;

        try{
            fileReader = new FileReader(getFile());

        }catch(FileNotFoundException e) {
            log.info("Error " + BarsException.NO_SUPPORTED_FILE + e);
            throw new ResponseStatusException
                    (HttpStatus.BAD_REQUEST,
                            BarsException.NO_SUPPORTED_FILE);
        }

        try(BufferedReader bufferedReader = new BufferedReader(fileReader)){
            String line;

            while((line = bufferedReader.readLine()) != null) {
                int billingCycle = Integer.parseInt
                        (line.substring(0, B_CYCLE_INDEX));

                try{
                    log.info("TextInputFileImpl - =>" +
                            " Processing Request ROW: " + row + " <= ");

                    if (billingCycle > MAX_BILLING_CYCLE ||
                            billingCycle < MIN_BILLING_CYCLE){
                        log.info("TextInputFileImpl - => " +
                                BarsException.BILLING_CYCLE_NOT_ON_RANGE + row);
                        throw new ResponseStatusException
                                (HttpStatus.BAD_REQUEST,
                                BarsException.BILLING_CYCLE_NOT_ON_RANGE + row);
                    }
                }catch (NumberFormatException exception){
                    log.info("TextInputFileImpl - => " +
                            BarsException.INVALID_BILLING_CYCLE + row);
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            BarsException.INVALID_BILLING_CYCLE + row);
                }

                try {
                    startDate = LocalDate.parse(line.substring
                                    (START_DATE_INDEX_A, START_DATE_INDEX_B),
                                    formatter);

                }catch (DateTimeParseException exception){
                    log.info("TextInputFileImpl - => " +
                            BarsException.INVALID_START_DATE_FORMAT + row);
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            BarsException.INVALID_START_DATE_FORMAT + row);
                }

                try{
                    endDate = LocalDate.parse(line.substring
                            (END_DATE_INDEX_A, END_DATE_INDEX_B), formatter);

                }catch (DateTimeParseException exception){
                    log.info("TextInputFileImpl - => " +
                            BarsException.INVALID_END_DATE_FORMAT + row);
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            BarsException.INVALID_END_DATE_FORMAT + row);
                }

                log.info("TextInputFileImpl - => " +
                        "Processing Request ROW: " + row + " - Successful");
                Request addRequest = new Request
                        (billingCycle, startDate, endDate);
                requests.add(addRequest);
                row ++;
                log.info("Request[billingCycle=" + billingCycle +
                        ", startDate=" + startDate +
                        ", endDate=" + endDate + "]");
            }

        }catch (IOException e){
            throw new RuntimeException(e);
        }


        return requests;
        }

}