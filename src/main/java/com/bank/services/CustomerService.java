package com.bank.services;

import com.bank.entity.Customer;
import com.bank.exception.NoDataFoundException;
import com.bank.exception.ResourceNotFoundException;
import com.bank.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    public Customer createCustomer(Customer customer) {
        Customer cust=customerRepository.save(customer);
        return cust;

    }

    public Customer getCustomerInfo(int actId) {
        Customer customer=customerRepository.findById(actId);
        if(customer!=null)
        {
            return customer;
        }
        throw new ResourceNotFoundException("Customer Not Found With Customer Id:"+actId);
    }

    public void deleteCustomer(int actId) {
        Customer customer=customerRepository.findById(actId);
        if(customer!=null)
        {
             customerRepository.deleteById(actId);
        }
        else {
            throw new ResourceNotFoundException("Account Not Found With Account Id:"+actId);
        }


    }

    public List<Customer> getCustomers() {
        List<Customer>list=customerRepository.findAll();
        if(list.size()!=0)
        {
            return list;
        }
        throw new NoDataFoundException("Customer Not Found in Our DataBase");

    }
}
