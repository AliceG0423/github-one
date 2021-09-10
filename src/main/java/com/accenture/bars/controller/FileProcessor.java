package com.accenture.bars.controller;

import com.accenture.bars.domain.Record;
import com.accenture.bars.domain.Request;
import com.accenture.bars.entity.Billing;
import com.accenture.bars.exception.BarsException;
import com.accenture.bars.factory.InputFileFactory;
import com.accenture.bars.file.AbstractInputFile;
import com.accenture.bars.repository.BillingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileProcessor {

    private static final Logger log = LoggerFactory.getLogger
            (BarsController.class);


    @Autowired
    private BillingRepository billingRepository;


    public FileProcessor() {
        //this is an empty constructor
    }

    public List<Request> execute(File file) throws BarsException {


        InputFileFactory inputFileF = InputFileFactory.getInstance();

        AbstractInputFile abstractInputFile = inputFileF.getInputFile(file);
        abstractInputFile.setFile(file);


        try {
            List<Request> requests = abstractInputFile.readFile();

            if(requests.isEmpty()){
                log.info("FileProcessor - => " +
                        BarsException.NO_REQUESTS_TO_READ);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        BarsException.NO_REQUESTS_TO_READ);

            }
            return requests;

        }catch(IOException e){
            throw new BarsException(BarsException.PATH_DOES_NOT_EXIST, e);
        }


    }


    public List<Record> retrieveRecordFromDB(List<Request> requests)
            throws BarsException {

        List<Record>  records = new ArrayList<>();
        for (Request request : requests) {

            Billing billing = billingRepository
                    .findByBillingCycleAndStartDateAndEndDate
                            (request.getBillingCycle()
                            , request.getStartDate()
                            , request.getEndDate());


            if (billing != null) {

                Record record = new Record();
                record.setFirstName
                        (billing.getAccountId().getCustomerId().getFirstName());
                record.setLastName
                        (billing.getAccountId().getCustomerId().getLastName());
                record.setBillingCycle(billing.getBillingCycle());
                record.setStartDate(billing.getStartDate());
                record.setEndDate(billing.getEndDate());
                record.setAmount(billing.getAmount());
                record.setAccountName(billing.getAccountId().getAccountName());

                records.add(record);

            }

        }


        if (records.isEmpty()) {
            log.info("FileProcessor - => " + BarsException.NO_RECORDS_TO_WRITE);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    BarsException.NO_RECORDS_TO_WRITE);
        } else {
            log.info("Successfully Processed Request File");
            return records;
        }
    }

}
