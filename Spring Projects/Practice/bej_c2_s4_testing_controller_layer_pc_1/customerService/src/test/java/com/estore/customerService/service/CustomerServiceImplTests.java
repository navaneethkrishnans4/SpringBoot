package com.estore.customerService.service;

import com.estore.customerService.domain.Customer;
import com.estore.customerService.domain.Product;
import com.estore.customerService.exception.CustomerAlreadyExistsException;
import com.estore.customerService.exception.CustomerNotFoundException;
import com.estore.customerService.repository.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(SpringExtension.class)
public class CustomerServiceImplTests {
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer1, customer2;
    private Product product1, product2;
    private List<Customer> customerList;

    @BeforeEach
    public void setUp(){
        product1 = new Product(1, "Apple iPhone 15 Pro", "120 Hz refresh rate 1179x2556 pixels 6.10-inch touchscreen display, powered by a 6-core Apple A17 Pro processor & 8GB of RAM.");
        product2 = new Product(2, "Samasung Galaxy S23", "120Hz refresh rate 6.1-inch Dynamic AMOLED display, a Snapdragon 8 Gen 2 processor, & 8 GB of RAM");

        customer1 = new Customer(1, "Steve Jobs", 2065829735, product1);
        customer2 = new Customer(2, "Lee Kun-hee", 2084440518, product2);

        customerList = new ArrayList<>();
    }

    @AfterEach
    public void tearDown(){
        customer1 = null;
        customer2 = null;

        product1 = null;
        product2 = null;

        customerList = null;
    }

    @Test
    @DisplayName("saving customer object")
    public void givenCustomerToSaveShouldReturnSavedCustomer() throws CustomerAlreadyExistsException {
        when(customerRepository.findById(customer1.getCustomerId())).thenReturn(Optional.ofNullable(null));
        when(customerRepository.save(customer1)).thenReturn(customer1);

        assertEquals(customer1, customerService.saveCustomer(customer1));

        verify(customerRepository, times(1)).findById(any());
        verify(customerRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("saving existing customer object")
    public void givenExistingCustomerToSaveShouldLeadToOneDBEntryOnly() {
        when(customerRepository.findById(customer1.getCustomerId())).thenReturn(Optional.ofNullable(customer1));

        assertThrows(CustomerAlreadyExistsException.class, ()->customerService.saveCustomer(customer1));

        verify(customerRepository, times(1)).findById(any());
        verify(customerRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("deleting customer object")
    public void givenCustomerToDeleteShouldDeleteCustomer() throws CustomerNotFoundException{
        when(customerRepository.findById(customer1.getCustomerId())).thenReturn(Optional.ofNullable(customer1));

        assertTrue(customerService.deleteCustomer(customer1.getCustomerId()));

        verify(customerRepository, times(1)).findById(any());
        verify(customerRepository, times(1)).deleteById(any());
    }

    @Test
    @DisplayName("deleting non-existent customer object")
    public void givenNonExistentCustomerToDeleteShouldNotAlterDB() {
        when(customerRepository.findById(customer1.getCustomerId())).thenReturn(Optional.ofNullable(null));

        assertThrows(CustomerNotFoundException.class, ()-> customerService.deleteCustomer(customer1.getCustomerId()));

        verify(customerRepository, times(1)).findById(any());
        verify(customerRepository, times(0)).deleteById(any());
    }

    @Test
    @DisplayName("retrieving all the customer objects")
    public void returnAllCustomerDetails() {
        customerList.add(customer1);
        customerList.add(customer2);

        when(customerRepository.findAll()).thenReturn(customerList);

        assertEquals(customerList, customerService.getAllCustomers());

        verify(customerRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("retrieving all the customer object from an empty collection")
    public void returnAllCustomerDetailsFromEmptyDB() {
        customerList.clear();

        when(customerRepository.findAll()).thenReturn(customerList);

        assertEquals(customerList, customerService.getAllCustomers());

        verify(customerRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("retrieving all the customer objects who bought given brand")
    public void givenBrandReturnAllCustomerDetails() {
        String brand = "Apple";
        customerList.add(customer1);

        when(customerRepository.getAllCxBoughtBrand(brand)).thenReturn(customerList);

        assertEquals(customerList, customerService.getAllCustomersWhoBoughtBrand(brand));

        verify(customerRepository, times(1)).getAllCxBoughtBrand(any());
    }

    @Test
    @DisplayName("retrieving all the customer objects who bought given brand negative scenario")
    public void givenNonExistentBrandReturnAllCustomerDetails() {
        String brand = "Google";
        customerList.clear();

        when(customerRepository.getAllCxBoughtBrand(brand)).thenReturn(customerList);

        assertEquals(customerList, customerService.getAllCustomersWhoBoughtBrand(brand));

        verify(customerRepository, times(1)).getAllCxBoughtBrand(any());
    }
}
