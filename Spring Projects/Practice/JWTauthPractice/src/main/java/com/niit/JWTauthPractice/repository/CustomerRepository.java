package com.niit.JWTauthPractice.repository;

import com.niit.JWTauthPractice.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Integer> {

  public Customer findByCustomerNameAndPassword(String customerName , String password);
}
