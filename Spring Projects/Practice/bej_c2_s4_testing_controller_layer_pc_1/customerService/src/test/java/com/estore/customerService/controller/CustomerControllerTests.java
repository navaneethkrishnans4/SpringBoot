package com.estore.customerService.controller;

import com.estore.customerService.domain.Customer;
import com.estore.customerService.domain.Product;
import com.estore.customerService.exception.CustomerAlreadyExistsException;
import com.estore.customerService.exception.CustomerNotFoundException;
import com.estore.customerService.service.CustomerServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerTests {
    private MockMvc mockMvc;

    @Mock
    private CustomerServiceImpl customerService;

    @InjectMocks
    private CustomerController customerController;

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

        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
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
    public void givenCustomerToSaveShouldReturnSavedCustomer() throws Exception{
        when(customerService.saveCustomer(any())).thenReturn(customer1);

        mockMvc.perform(post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonToString(customer1)))
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());

        verify(customerService, times(1)).saveCustomer(any());
    }

    @Test
    @DisplayName("saving existing customer object")
    public void givenExistingCustomerToSaveShouldLeadToOneDBEntryOnly() throws Exception{
        when(customerService.saveCustomer(any())).thenThrow(CustomerAlreadyExistsException.class);

        mockMvc.perform(post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonToString(customer1)))
                .andExpect(status().isConflict())
                .andDo(MockMvcResultHandlers.print());

        verify(customerService, times(1)).saveCustomer(any());
    }

    @Test
    @DisplayName("deleting customer object")
    public void givenCustomerToDeleteShouldDeleteCustomer() throws Exception{
        when(customerService.deleteCustomer(anyInt())).thenReturn(true);

        mockMvc.perform(delete("/api/v1/customers/1"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        verify(customerService, times(1)).deleteCustomer(anyInt());
    }

    @Test
    @DisplayName("deleting non-existent customer object")
    public void givenNonExistentCustomerToDeleteShouldNotAlterDB() throws Exception{
        when(customerService.deleteCustomer(anyInt())).thenThrow(CustomerNotFoundException.class);

        mockMvc.perform(delete("/api/v1/customers/1"))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());

        verify(customerService, times(1)).deleteCustomer(anyInt());
    }

    @Test
    @DisplayName("retrieving all the customer objects")
    public void returnAllCustomerDetails() throws Exception{
        customerList.add(customer1);
        customerList.add(customer2);

        when(customerService.getAllCustomers()).thenReturn(customerList);

        mockMvc.perform(get("/api/v1/customers"))
                .andExpect(content().json(jsonToString(customerList)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        verify(customerService,times(1)).getAllCustomers();
    }

    @Test
    @DisplayName("retrieving all the customer object from an empty collection")
    public void returnAllCustomerDetailsFromEmptyDB() throws Exception{
        customerList.clear();

        when(customerService.getAllCustomers()).thenReturn(customerList);

        mockMvc.perform(get("/api/v1/customers"))
                .andExpect(content().json(jsonToString(customerList)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        verify(customerService,times(1)).getAllCustomers();
    }

    @Test
    @DisplayName("retrieving all the customer objects who bought given brand")
    public void givenBrandReturnAllCustomerDetails() throws Exception{
        String brand = "Apple";
        customerList.add(customer1);

        when(customerService.getAllCustomersWhoBoughtBrand(any())).thenReturn(customerList);

        mockMvc.perform(get("/api/v1/customers/" + brand))
                .andExpect(content().json(jsonToString(customerList)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        verify(customerService,times(1)).getAllCustomersWhoBoughtBrand(any());
    }

    @Test
    @DisplayName("retrieving all the customer objects who bought given brand negative scenario")
    public void givenNonExistentBrandReturnAllCustomerDetails() throws Exception{
        String brand = "Google";
        customerList.clear();

        when(customerService.getAllCustomersWhoBoughtBrand(any())).thenReturn(customerList);

        mockMvc.perform(get("/api/v1/customers/" + brand))
                .andExpect(content().json(jsonToString(customerList)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        verify(customerService,times(1)).getAllCustomersWhoBoughtBrand(any());
    }

    private static String jsonToString(final Object ob) throws JsonProcessingException {
        String result;
        try {
            ObjectMapper mapper = new ObjectMapper();
            result = mapper.writeValueAsString(ob);
        } catch(JsonProcessingException e) {
            result = "JSON processing error";
        }

        return result;
    }
}
