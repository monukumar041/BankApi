package com.bank.services;

import com.bank.entity.Logger;
import com.bank.exception.ResourceNotFoundException;
import com.bank.repository.LoggerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoggerService {

    @Autowired
    private LoggerRepository loggerRepository;
    public Logger addLog(Logger logger) {
        return loggerRepository.save(logger);
    }

    public Logger showLog(int actId) {
        Logger logger=loggerRepository.findById(actId);
        if(logger!=null)
        {
            return logger;
        }
        throw new ResourceNotFoundException("Log Not Found With Account Id:"+actId);
    }

    public void deleteLog(int actId) {
        Logger logger=loggerRepository.findById(actId);
        if(logger!=null)
        {
            loggerRepository.deleteById(actId);
        }
        else {

            throw new ResourceNotFoundException("Log Not Found With Account Id:" + actId);
        }

    }
}
