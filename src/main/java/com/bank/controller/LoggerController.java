package com.bank.controller;
import com.bank.entity.Logger;
import com.bank.services.LoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoggerController {
    @Autowired
    private LoggerService loggerService;

    public Logger addLog(Logger logger)
    {
        return loggerService.addLog(logger);
    }

    @GetMapping("/account/{actId}/log")
    public Logger showLog(@PathVariable("actId")int actId)
    {
        return loggerService.showLog(actId);
    }

    public void deleteLog(int actId)
    {
         loggerService.deleteLog(actId);
    }
}
