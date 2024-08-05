package com.niit.RESTfullwithMongoDB.service;

import com.niit.RESTfullwithMongoDB.domain.Customer;
import com.niit.RESTfullwithMongoDB.exception.CustomerAlreadyExistException;
import com.niit.RESTfullwithMongoDB.exception.CustomerNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface CustomerService {
    Customer saveCustomer(Customer customer) throws CustomerAlreadyExistException;
    List<Customer> getAllCustomer();
    List<Customer> getCustomerByCustomerName(String customerName);
    List<Customer> findAllCustomerByProductName(String productName) throws CustomerNotFoundException ;
    boolean deleteCustomer(String customerId) throws CustomerNotFoundException;
    Optional<Customer> getCustomerByCustomerId(String customerId) throws CustomerNotFoundException;
}
