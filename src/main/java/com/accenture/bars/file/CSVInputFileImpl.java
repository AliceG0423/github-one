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

public class CSVInputFileImpl extends AbstractInputFile{


    private static final Logger log =  LoggerFactory.getLogger
            (CSVInputFileImpl.class);


    public CSVInputFileImpl() {
        //this is an empty constructor
    }

    @Override
    public List<Request> readFile() throws IOException, BarsException {
        List<Request> request = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        LocalDate startDate;
        LocalDate endDate;
        FileReader fileReader;
        int row = 1;
        final int B_CYCLE_INDEX = 2;

        try {
            fileReader = new FileReader(getFile().toString());

        } catch (FileNotFoundException e) {
            log.info("Error " + BarsException.NO_SUPPORTED_FILE + e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    BarsException.NO_SUPPORTED_FILE);
        }

        try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line;

            while ((line = bufferedReader.readLine()) != null) {

                log.debug(line);
                String[] data = line.split(",");
                int billingCycle = Integer.parseInt(data[0]);


                try {
                    log.info("CSVInputFileImpl - => Processing Request ROW: " +
                            row + " <= ");

                    if (billingCycle > MAX_BILLING_CYCLE ||
                            billingCycle < MIN_BILLING_CYCLE) {
                        log.info("CSVInputFileImpl - => " +
                                BarsException.BILLING_CYCLE_NOT_ON_RANGE +
                                row + " <= ");
                        throw new ResponseStatusException
                                (HttpStatus.BAD_REQUEST,
                                BarsException.BILLING_CYCLE_NOT_ON_RANGE + row);
                    }

                } catch (NumberFormatException e) {
                    log.info("CSVInputFileImpl - => " +
                            BarsException.INVALID_BILLING_CYCLE + row + " <= ");
                    throw new ResponseStatusException
                            (HttpStatus.BAD_REQUEST,
                            BarsException.INVALID_BILLING_CYCLE + row);
                }

                try {
                    startDate = LocalDate.parse(data[1], formatter);
                } catch (DateTimeParseException e) {
                    log.info("CSVInputFileImpl - => " +
                            BarsException.INVALID_START_DATE_FORMAT +
                            row + " <= ");
                    throw new ResponseStatusException
                            (HttpStatus.BAD_REQUEST,
                            BarsException.INVALID_START_DATE_FORMAT +
                                    row);
                }

                try {
                    endDate = LocalDate.parse(data[B_CYCLE_INDEX], formatter);
                } catch (DateTimeParseException e) {
                    log.info("CSVInputFileImpl - => " +
                            BarsException.INVALID_END_DATE_FORMAT +
                            row + " <= ");
                    throw new ResponseStatusException
                            (HttpStatus.BAD_REQUEST,
                            BarsException.INVALID_END_DATE_FORMAT + row);

                }

                log.info("CSVInputFileImpl - =>" +
                        " Processing Request ROW: " +
                        row + " - Successful");

                Request addRequest = new Request(billingCycle,
                        startDate, endDate);
                request.add(addRequest);
                row++;
                log.info("Request[billingCycle=" + billingCycle +
                        ", startDate=" + startDate +
                        ", endDate=" + endDate + "]");
            }


        } catch (IOException e) {
            throw new RuntimeException(e);

        }

        return  request;
        
    }
}
