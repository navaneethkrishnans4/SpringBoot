package com.estore.customerService.repository;

import com.estore.customerService.domain.Customer;
import com.estore.customerService.domain.Product;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataMongoTest
public class CustomerRepositoryTest {
    @Autowired
    private CustomerRepository customerRepository;
    private Customer customer;
    private Product product;

    @BeforeEach
    public void setUp() {
        product = new Product(12, "Apple iPhone 15 Pro", "120 Hz refresh rate 1179x2556 pixels 6.10-inch touchscreen display, powered by a 6-core Apple A17 Pro processor & 8GB of RAM.");
        customer = new Customer(7, "Steve Jobs", 2065829735, product);
    }

    @AfterEach
    public void tearDown() {
        product = null;
        customer = null;
        customerRepository.deleteAll();
    }

    @Test
    @DisplayName("Test case for saving customer object")
    public void givenCustomerToSaveShouldReturnSavedCustomer() {
        customerRepository.save(customer);
        Customer returnedCx = customerRepository.findById(customer.getCustomerId()).get();

        assertNotNull(returnedCx);
        assertEquals(customer.getCustomerId(), returnedCx.getCustomerId());
    }

    @Test
    @DisplayName("Test case for saving existing customer object")
    public void givenExistingCustomerToSaveShouldLeadToOneDBEntryOnly() {
        customerRepository.save(customer);
        Customer returnedCx = customerRepository.findById(customer.getCustomerId()).get();

        assertNotNull(returnedCx);
        assertEquals(customer.getCustomerId(), returnedCx.getCustomerId());

        customerRepository.save(customer);

        assertEquals(1, customerRepository.findAll().size());
    }

    @Test
    @DisplayName("Test case for deleting customer object")
    public void givenCustomerToDeleteShouldDeleteCustomer() {
        customerRepository.save(customer);
        Customer returnedCx = customerRepository.findById(customer.getCustomerId()).get();
        customerRepository.delete(returnedCx);
        assertEquals(Optional.empty(), customerRepository.findById(customer.getCustomerId()));
    }

    @Test
    @DisplayName("Test case for deleting non-existent customer object")
    public void givenNonExistentCustomerToDeleteShouldNotAlterDB() {
        customerRepository.save(customer);
        customerRepository.deleteById(1);
        assertEquals(1, customerRepository.findAll().size());
    }

    @Test
    @DisplayName("Test case for retrieving all the customer objects")
    public void returnAllCustomerDetails() {
        customerRepository.save(customer);

        int newCxId = 10;
        String newCxName = "Jony Ive";
        customer.setCustomerId(newCxId);
        customer.setCustomerName(newCxName);
        customerRepository.save(customer);

        List<Customer> cxList = customerRepository.findAll();
        assertEquals(2, cxList.size());
        assertEquals(newCxId, cxList.get(1).getCustomerId());
        assertEquals(newCxName, cxList.get(1).getCustomerName());
    }

    @Test
    @DisplayName("Test case for retrieving all the customer object from an empty collection")
    public void returnAllCustomerDetailsFromEmptyDB() {
        List<Customer> cxList = customerRepository.findAll();
        assertEquals(0, cxList.size());
    }

    @Test
    @DisplayName("Test case for retrieving all the customer objects who bought given brand")
    public void givenBrandReturnAllCustomerDetails() {
        String brandName = "Apple";
        customerRepository.save(customer);
        List<Customer> cxList = customerRepository.getAllCxBoughtBrand(brandName);
        assertEquals(1, cxList.size());
        assertEquals(customer.getCustomerId(), cxList.get(0).getCustomerId());
    }

    @Test
    @DisplayName("Test case for retrieving all the customer objects who bought given brand")
    public void givenNonExistentBrandReturnAllCustomerDetails() {
        String brandName = "Samsung";
        customerRepository.save(customer);
        List<Customer> cxList = customerRepository.getAllCxBoughtBrand(brandName);
        assertEquals(0, cxList.size());
    }
}
