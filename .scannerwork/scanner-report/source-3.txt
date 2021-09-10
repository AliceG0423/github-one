package com.accenture.bars.controller;

import com.accenture.bars.domain.Record;
import com.accenture.bars.exception.BarsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.util.List;

@RestController
public class BarsController {


    private static final Logger log = LoggerFactory.getLogger
            (BarsController.class);
    @Autowired
    private FileProcessor fileProcessor;

    public BarsController() {
         //this is an empty constructor
     }

    @GetMapping("/bars")
    public ResponseEntity<List<Record>> requestBilling
            (@RequestParam("filePath") String fileName)
            throws BarsException{


        File file = new File("C:/BARS_TEST/" + fileName);
        log.info("BarsController - => FilePath: " + fileName);
        log.info("BarsController - File To Process: " + file);


        if(!file.exists()){
            log.info("BarsController - =>  " +
                    BarsException.PATH_DOES_NOT_EXIST);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    BarsException.PATH_DOES_NOT_EXIST);
        }

        return new ResponseEntity(fileProcessor.retrieveRecordFromDB
                (fileProcessor.execute(file)),
                HttpStatus.OK);

    }

}
