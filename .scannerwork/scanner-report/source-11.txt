package com.accenture.bars.factory;

import com.accenture.bars.exception.BarsException;
import com.accenture.bars.file.AbstractInputFile;
import com.accenture.bars.file.CSVInputFileImpl;
import com.accenture.bars.file.TextInputFileImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;


public class InputFileFactory {

    private static final Logger log = LoggerFactory.getLogger
            (TextInputFileImpl.class);


    private  static InputFileFactory factory;

    private InputFileFactory() {}

    public static InputFileFactory getInstance() {
        if (factory == null) {
            factory = new InputFileFactory();
        }
        return factory;
    }


    public AbstractInputFile getInputFile(File file) throws BarsException {


        if (file.getName().endsWith(".txt")) {
            return new TextInputFileImpl();
        } else if (file.getName().endsWith(".csv")) {
            return new CSVInputFileImpl();
        } else {

            log.info("InputFileFactory - => " +
                    BarsException.FILE_NOT_SUPPORTED);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    BarsException.FILE_NOT_SUPPORTED);
        }
    }


}
