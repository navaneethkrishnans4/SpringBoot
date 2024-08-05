package com.estore.customerService.service;

import com.estore.customerService.domain.Customer;
import com.estore.customerService.exception.CustomerAlreadyExistsException;
import com.estore.customerService.exception.CustomerNotFoundException;

import java.util.List;

public interface ICustomerService {
    Customer saveCustomer(Customer customer) throws CustomerAlreadyExistsException;
    boolean deleteCustomer(int id) throws CustomerNotFoundException;
    List<Customer> getAllCustomers() throws CustomerNotFoundException;
    List<Customer> getAllCustomersWhoBoughtBrand(String brand) throws CustomerNotFoundException;
}
