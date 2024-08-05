package com.niit.RESTfullwithMongoDB.controller;

import com.niit.RESTfullwithMongoDB.domain.Customer;
import com.niit.RESTfullwithMongoDB.exception.CustomerAlreadyExistException;
import com.niit.RESTfullwithMongoDB.exception.CustomerNotFoundException;
import com.niit.RESTfullwithMongoDB.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/v1"})
public class CustomerController {
    private CustomerService customerService;
    ResponseEntity responseEntity;
    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }
    @PostMapping({"/customer"})
    ResponseEntity<?> saveCustomer(@RequestBody Customer customer) throws CustomerAlreadyExistException {
        try {
            Customer savedCustomer = this.customerService.saveCustomer(customer);
            this.responseEntity = new ResponseEntity(savedCustomer, HttpStatus.CREATED);
        } catch (CustomerAlreadyExistException var3) {
            throw new CustomerAlreadyExistException();
        } catch (Exception var4) {
            this.responseEntity = new ResponseEntity(var4.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return this.responseEntity;
    }

    @DeleteMapping({"/customer/{customerId}"})
    ResponseEntity<?> deleteCustomer(@PathVariable String customerId) throws CustomerNotFoundException {
        System.out.println("trying to delete the data");

        try {
            this.customerService.deleteCustomer(customerId);
            this.responseEntity = new ResponseEntity("Successfully deleted", HttpStatus.OK);
        } catch (CustomerNotFoundException var3) {
            throw new CustomerNotFoundException();
        } catch (Exception var4) {
            this.responseEntity = new ResponseEntity(var4.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return this.responseEntity;
    }

    @GetMapping({"/customer"})
    ResponseEntity<?> getAllCustomer() {
        return new ResponseEntity(this.customerService.getAllCustomer(), HttpStatus.OK);
    }

    @GetMapping({"/product/{productName}"})
    ResponseEntity<?> getCustomerByProductName(@PathVariable String productName) throws CustomerNotFoundException {
        try {
            this.responseEntity = new ResponseEntity(this.customerService.findAllCustomerByProductName(productName), HttpStatus.OK);
        } catch (CustomerNotFoundException var3) {
            throw new CustomerNotFoundException();
        } catch (Exception var4) {
            this.responseEntity = new ResponseEntity(var4.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return this.responseEntity;
    }
    @GetMapping({"/customer/{customerId}"})
    ResponseEntity<?> getCustomerByCustomerId(@PathVariable String customerId) throws CustomerNotFoundException {
        try {
            this.responseEntity = new ResponseEntity(this.customerService.getCustomerByCustomerId(customerId), HttpStatus.OK);
        } catch (CustomerNotFoundException var3) {
            throw new CustomerNotFoundException();
        } catch (Exception var4) {
            this.responseEntity = new ResponseEntity(var4.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return this.responseEntity;
    }

    @GetMapping("/customer/customerName/{customerName}")
    public ResponseEntity<?> getTrackByTrackArtist(@PathVariable String customerName)
    {
        List<Customer> list = customerService.getCustomerByCustomerName(customerName);
        return new ResponseEntity<>(list,HttpStatus.FOUND);
    }

}
