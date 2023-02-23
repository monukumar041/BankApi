package com.bank.controller;

import com.bank.entity.Logger;
import com.bank.services.LoggerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;

class LoggerControllerTest {


    Logger logger=new Logger(1234,"Transferred","Success",1000,500);
    Logger logger1=new Logger(1235,"Cradited","Success",1000,1500);
    Logger logger2=new Logger(1236,"Transferred","Success",10000,5000);
    ObjectMapper objectMapper=new ObjectMapper();
    ObjectWriter objectWriter=objectMapper.writer();

    @Mock
    private LoggerService loggerService;
    @InjectMocks
    private LoggerController loggerController;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc= MockMvcBuilders.standaloneSetup(loggerController).build();
    }

    @Test
    void addLogSuccess() throws Exception{
        given(loggerService.addLog(logger)).willReturn(logger);
        Logger logger3=loggerController.addLog(logger);
        assertEquals(logger.getFinalBal(),logger3.getFinalBal());


    }

    @Test
    void showLogSuccess() throws Exception{
        given(loggerService.showLog(logger.getActId())).willReturn(logger);
        Logger log=loggerController.showLog(logger.getActId());
        assertEquals(logger.getTransType(),log.getTransType());
        mockMvc.perform(MockMvcRequestBuilders
                .get("/account/1234/log")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.transType",is(logger.getTransType())));
    }

    @Test
    void deleteLogSuccess() throws Exception {
        doNothing().when(loggerService).deleteLog(logger.getActId());
        loggerController.deleteLog(logger.getActId());
        verify(loggerService).deleteLog(logger.getActId());
    }
}