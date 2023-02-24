package com.bank.controller;
import com.bank.entity.Customer;
import com.bank.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class CustomerController {
    @Autowired
    private AccountController accountController;
    @Autowired
    private CustomerService customerService;
    @PostMapping("/customer")
    public Customer createCustomer(@RequestBody Customer customer)
    {
        return customerService.createCustomer(customer);
    }

    @GetMapping("/customer/{actId}")
    public Customer getCustomerInfo(@PathVariable("actId")int actId)
    {
        Customer customer=customerService.getCustomerInfo(actId);
        return customer;
    }

    @DeleteMapping("/customer/{actId}")
    public void deleteCustomer(@PathVariable("actId")int actId)
    {

        customerService.deleteCustomer(actId);
    }

    @GetMapping("/customer")
    public List<Customer>getCustomers()
    {
        List<Customer>list=customerService.getCustomers();
        return list;
    }
}
