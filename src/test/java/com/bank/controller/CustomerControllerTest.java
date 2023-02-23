package com.bank.controller;

import com.bank.entity.Account;
import com.bank.entity.Customer;
import com.bank.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;

class CustomerControllerTest {
    @InjectMocks
    private CustomerController customerController;
    @Mock
    private CustomerService customerService;
    private MockMvc mockMvc;
    ObjectMapper objectMapper=new ObjectMapper();
    ObjectWriter objectWriter=objectMapper.writer();


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc= MockMvcBuilders.standaloneSetup(customerController).build();
    }
    Customer customer=new Customer(1234,"Mr Monu Kumar","Ranchi","Jharkhand","India",56789,"monu@123",new Account(52,1000,"Active"));
    Customer customer1=new Customer(1235,"Mr Rahul, Kumar","Ranchi","Jharkhand","India",91621,"rahul@123",new Account(53,2000,"Active"));
    @Test
    void createCustomerSuccess() throws Exception {
        given(customerService.createCustomer(any(Customer.class))).willAnswer(invocation -> invocation.getArgument(0));
        String content=objectWriter.writeValueAsString(customer);
        MockHttpServletRequestBuilder mockRequest= MockMvcRequestBuilders
                .post("/customer")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.custName",is(customer.getCustName())));


    }

    @Test
    void getCustomerInfoSuccess() throws Exception {
        given(customerService.getCustomerInfo(customer.getActId())).willReturn(customer);
        Customer customer2=customerController.getCustomerInfo(customer.getActId());
        assertEquals(customer.getPhoneNo(),customer2.getPhoneNo());
        mockMvc.perform(MockMvcRequestBuilders
                .get("/customer/1234")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.custName",is(customer.getCustName())));
    }

    @Test
    void deleteCustomerSuccess() throws Exception{
        doNothing().when(customerService).deleteCustomer(customer.getActId());
        customerController.deleteCustomer(customer.getActId());
        verify(customerService).deleteCustomer(customer.getActId());
    }

    @Test
    void getCustomersSuccess() throws Exception {
        List<Customer>list=new ArrayList<>(Arrays.asList(customer,customer1));
        given(customerService.getCustomers()).willReturn(list);
        List<Customer>list1=customerController.getCustomers();
        assertEquals(list.size(),list1.size());
        mockMvc.perform(MockMvcRequestBuilders
                .get("/customer")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[1].custName",is(customer1.getCustName())));
    }
}