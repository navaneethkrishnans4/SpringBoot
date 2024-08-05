package com.niit.RESTfullwithMongoDB.repository;

import com.niit.RESTfullwithMongoDB.domain.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends MongoRepository<Customer,String> {


    @Query("{'product.productName' :{$in:[?0]}}")
    List<Customer> findAllCustomerByProductName(String productName) ;
    @Query("{'customerId': ?0}")
    Optional<Customer> findCustomerByCustomerId(String customerId);
    List<Customer> findByCustomerName(String customerName);
}
