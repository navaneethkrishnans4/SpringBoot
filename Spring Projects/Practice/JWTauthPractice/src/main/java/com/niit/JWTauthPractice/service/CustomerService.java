package com.niit.JWTauthPractice.service;

import com.niit.JWTauthPractice.domain.Customer;
import com.niit.JWTauthPractice.exception.UserNotFoundException;

import java.util.List;


public interface CustomerService {

  public Customer saveCustomer(Customer user);
  public Customer findByCustomerNameAndPassword(String customerName , String password) throws UserNotFoundException;
  List<Customer> getAllCustomers();

//  boolean validateUser(String username, String password) throws UserNotFoundException;

}
