package com.bank.services;

import com.bank.entity.Logger;
import com.bank.exception.ResourceNotFoundException;
import com.bank.repository.LoggerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.BDDMockito.*;


class LoggerServiceTest {

    @InjectMocks
    private LoggerService loggerService;
    @Mock
    private LoggerRepository loggerRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addLog() throws Exception{
        Logger log=new Logger(1234,"Transferred","Success",1000,500);
       given(loggerRepository.save(log)).willReturn(log);
       Logger logger=loggerService.addLog(log);
       assertEquals(log.getFinalBal(),logger.getFinalBal());

    }

    @Test
    void showLog_IfExist()
    {
        Logger log=new Logger(1234,"Transferred","Success",1000,500);
        when(loggerRepository.findById(log.getActId())).thenReturn(log);
        Logger logger=loggerService.showLog(log.getActId());
        assertEquals(log.getFinalBal(),logger.getFinalBal());
        verify(loggerRepository).findById(log.getActId());
    }
    @Test
    void showLog_IfNotExist()
    {
        int logId=123;
        when(loggerRepository.findById(any())).thenReturn(null);
        assertThrows(ResourceNotFoundException.class,()->loggerService.showLog(logId));
        verify(loggerRepository).findById(logId);
    }


    @Test
    void deleteLog_IfExist()
    {
        Logger log=new Logger(1234,"Transferred","Success",1000,500);
        when(loggerRepository.findById(log.getActId())).thenReturn(log);
        loggerService.deleteLog(log.getActId());
        verify(loggerRepository).findById(log.getActId());
    }
    @Test
    void deleteLog_IfNotExist()
    {
        int logId=123812;
        when(loggerRepository.findById(any())).thenReturn(null);
        assertThrows(ResourceNotFoundException.class,()->loggerService.deleteLog(logId));
        verify(loggerRepository).findById(logId);
    }
}