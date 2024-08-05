package com.estore.customerService.service;

import com.estore.customerService.domain.Customer;
import com.estore.customerService.exception.CustomerAlreadyExistsException;
import com.estore.customerService.exception.CustomerNotFoundException;
import com.estore.customerService.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CustomerServiceImpl implements ICustomerService{
    private CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer saveCustomer(Customer customer) throws CustomerAlreadyExistsException {
        if(customerRepository.findById(customer.getCustomerId()).isPresent())
        {
            throw new CustomerAlreadyExistsException();
        }

        return customerRepository.save(customer);
    }

    @Override
    public boolean deleteCustomer(int id) throws CustomerNotFoundException {
        if(customerRepository.findById(id).isEmpty()){
            throw new CustomerNotFoundException();
        }

        customerRepository.deleteById(id);
        return true;
    }

    @Override
    public List<Customer> getAllCustomers(){
        return customerRepository.findAll();
    }

    @Override
    public List<Customer> getAllCustomersWhoBoughtBrand(String brand){
        return customerRepository.getAllCxBoughtBrand(brand);
    }
}
