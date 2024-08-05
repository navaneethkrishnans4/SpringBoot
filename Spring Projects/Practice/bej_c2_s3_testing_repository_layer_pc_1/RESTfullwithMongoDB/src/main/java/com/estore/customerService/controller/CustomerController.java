package com.estore.customerService.controller;

import com.estore.customerService.domain.Customer;
import com.estore.customerService.exception.CustomerAlreadyExistsException;
import com.estore.customerService.exception.CustomerNotFoundException;
import com.estore.customerService.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class CustomerController {
    private ICustomerService customerService;
    private ResponseEntity responseEntity;

    @Autowired
    public CustomerController(ICustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/customers")
    public ResponseEntity saveCustomer(@RequestBody Customer customer) throws CustomerAlreadyExistsException{
        try{
            responseEntity = new ResponseEntity<>(customerService.saveCustomer(customer), HttpStatus.CREATED);
        }catch (CustomerAlreadyExistsException ex){
            throw new CustomerAlreadyExistsException();
        }catch (Exception ex){
            responseEntity = new ResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
         return responseEntity;
    }

    @GetMapping("/customers")
    public ResponseEntity getAllCustomers(){
        try{
            responseEntity = new ResponseEntity(customerService.getAllCustomers(), HttpStatus.OK);
        }catch (Exception ex){
            responseEntity = new ResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    }

    @DeleteMapping("/customers/{customerId}")
    public ResponseEntity deleteCustomer(@PathVariable("customerId") int customerId) throws CustomerNotFoundException{
        try{
            customerService.deleteCustomer(customerId);
            responseEntity = new ResponseEntity("Successfully deleted!", HttpStatus.OK);
        }catch (CustomerNotFoundException ex){
            throw new CustomerNotFoundException();
        }catch (Exception ex){
            responseEntity = new ResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    }

    @GetMapping("/customers/{brand}")
    public ResponseEntity getAllCustomersWhoBoughtBrand(@PathVariable("brand") String brand)  throws CustomerNotFoundException{
        try {
            responseEntity = new ResponseEntity(customerService.getAllCustomersWhoBoughtBrand(brand), HttpStatus.OK);
        }catch (CustomerNotFoundException ex){
            throw new CustomerNotFoundException();
        }catch (Exception ex){
            responseEntity = new ResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    }
}
