package com.bank.services;

import com.bank.entity.Account;
import com.bank.entity.Customer;
import com.bank.exception.NoDataFoundException;
import com.bank.exception.ResourceNotFoundException;
import com.bank.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CustomerServiceTest {



    @Mock
    private CustomerRepository customerRepository;
    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCustomer()  {
        Customer customer=new Customer(1234,"Mr Gopal Agarwal","ranchi","jharkhand","india",5678,"gopla@123",new Account(54,3000,"Active"));
        given(customerRepository.save(customer)).willReturn(customer);
        Customer customer1=customerService.createCustomer(customer);
        assertEquals(customer.getPhoneNo(),customer1.getPhoneNo());
        verify(customerRepository).save(customer);
    }

    @Test
    void getCustomerInfo_IfExist()
    {
        int custId=12;
        Customer customer2=new Customer(custId,"monu","ranchi","jharkhand","india",4567,"mk@234",new Account(1,7000,"Active"));
        when(customerRepository.findById(custId)).thenReturn(customer2);
        Customer customer3=customerService.getCustomerInfo(custId);
        assertEquals(customer3.getCity(),customer2.getCity());
        verify(customerRepository).findById(custId);
    }
    @Test
    void getCustomerInfo_IfNotExist()
    {
        int custId=1231;
        when(customerRepository.findById(any())).thenReturn(null);
        assertThrows(ResourceNotFoundException.class,()->customerService.getCustomerInfo(custId));
        verify(customerRepository).findById(custId);

    }

    @Test
    void deleteCustomer_IfExist()
    {
        int custId=12;
        Customer customer2=new Customer(custId,"monu","ranchi","jharkhand","india",4567,"mk@234",new Account(1,7000,"Active"));
        when(customerRepository.findById(custId)).thenReturn(customer2);
        customerService.deleteCustomer(custId);
        verify(customerRepository).findById(custId);
    }
    @Test
    void deleteCustomer_IfNotExist()
    {
        int custId=1213;
        when(customerRepository.findById(anyInt())).thenReturn(null);
        assertThrows(ResourceNotFoundException.class,()->customerService.deleteCustomer(custId));
        verify(customerRepository).findById(custId);
    }


    @Test
    void getCustomers_Valid()
    {
        int custId=12;
        Customer customer2=new Customer(custId,"monu","ranchi","jharkhand","india",4567,"mk@234",new Account(1,7000,"Active"));
        List<Customer>list=new ArrayList<>(Arrays.asList(customer2));
        when(customerRepository.findAll()).thenReturn(list);
        List<Customer>list1=customerService.getCustomers();
        assertEquals(list.size(),list1.size());
        verify(customerRepository).findAll();
    }
    @Test void getCustomer_Invalid()
    {
        List<Customer>list=new ArrayList<>();
        when(customerRepository.findAll()).thenReturn(list);
        assertThrows(NoDataFoundException.class,()->customerService.getCustomers());
        verify(customerRepository).findAll();
    }
}