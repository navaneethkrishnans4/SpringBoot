package com.niit.JWTauthPractice.service;


import com.niit.JWTauthPractice.domain.Customer;
import com.niit.JWTauthPractice.domain.Customer;
import com.niit.JWTauthPractice.exception.UserNotFoundException;
import com.niit.JWTauthPractice.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CustomerServiceImpl implements CustomerService {
    private CustomerRepository userRepository;

    @Autowired
  public CustomerServiceImpl(CustomerRepository userRepository){
    this.userRepository = userRepository;

    }


  @Override
  public Customer saveCustomer(Customer customer) {
        return userRepository.save(customer);
    }

  @Override
  public Customer findByCustomerNameAndPassword(String username , String password) throws UserNotFoundException {
         Customer user =  userRepository.findByCustomerNameAndPassword(username , password);
         if(user == null){
           throw new UserNotFoundException();
         }
         return user;

  }

    @Override
    public List<Customer> getAllCustomers() {
      return userRepository.findAll();
    }

}
