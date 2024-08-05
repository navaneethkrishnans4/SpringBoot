package com.niit.RESTfullwithMongoDB.service;

import com.niit.RESTfullwithMongoDB.domain.Customer;
import com.niit.RESTfullwithMongoDB.exception.CustomerAlreadyExistException;
import com.niit.RESTfullwithMongoDB.exception.CustomerNotFoundException;
import com.niit.RESTfullwithMongoDB.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService{

    CustomerRepository customerRepository;
    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository)
    {
        this.customerRepository = customerRepository;
    };
    @Override
    public Customer saveCustomer(Customer customer) throws CustomerAlreadyExistException {
        if(this.customerRepository.findById(customer.getCustomerId()).isPresent())
        {
            throw new CustomerAlreadyExistException();
        }
        else {
            return this.customerRepository.save(customer);
        }

    }

    @Override
    public List<Customer> getAllCustomer() {
        return this.customerRepository.findAll();
    }

    @Override
    public List<Customer> findAllCustomerByProductName(String productName) throws CustomerNotFoundException{
        if (this.customerRepository.findAllCustomerByProductName(productName).isEmpty()) {
            throw new CustomerNotFoundException();
        } else {
            return this.customerRepository.findAllCustomerByProductName(productName);
        }
    }

    public boolean deleteCustomer(String customerId) throws CustomerNotFoundException {
        boolean flag = false;
        Optional<Customer> customerOptional = this.customerRepository.findById(customerId);
        if (customerOptional.isEmpty()) {
            throw new CustomerNotFoundException();
        } else {
            this.customerRepository.deleteById(customerId);
            flag = true;
            return flag;
        }
    }

    @Override
    public Optional<Customer> getCustomerByCustomerId(String customerId) {
        return this.customerRepository.findCustomerByCustomerId(customerId);
    }

    @Override
    public List<Customer> getCustomerByCustomerName(String customerName) {
        return (List<Customer>) customerRepository.findByCustomerName(customerName);
    }


}
