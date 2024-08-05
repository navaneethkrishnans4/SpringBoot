package com.niit.JWTauthPractice.controller;


import com.niit.JWTauthPractice.domain.Customer;
import com.niit.JWTauthPractice.service.CustomerService;

import com.niit.JWTauthPractice.exception.UserNotFoundException;
import com.niit.JWTauthPractice.service.SecurityTokenGenerator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
public class UserController {

  private ResponseEntity responseEntity;
  private CustomerService userService;
  private SecurityTokenGenerator securityTokenGenerator;


  @Autowired
  public UserController(CustomerService userService , SecurityTokenGenerator securityTokenGenerator)
  {
    this.userService = userService;
    this.securityTokenGenerator = securityTokenGenerator;
  }


  //Should only give username and password
  @PostMapping("/login")
  public ResponseEntity loginUser(@RequestBody Customer user) throws UserNotFoundException {

    Map<String, String> map = null;
    try {
      Customer userObj = userService.findByCustomerNameAndPassword(user.getCustomerName(), user.getPassword());
      if (userObj.getCustomerName().equals(user.getCustomerName())) {
        map = securityTokenGenerator.generateToken(user);
      }
      responseEntity = new ResponseEntity(map, HttpStatus.OK);
    }
  catch(UserNotFoundException e){
      throw new UserNotFoundException();
  }
    catch (Exception e){
      responseEntity = new ResponseEntity("Try after sometime!!!", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return responseEntity;
  }

  // first step - register the user
  @PostMapping("/register")
  public ResponseEntity saveUser(@RequestBody Customer user) {

    Customer createdUser = userService.saveCustomer(user);
      return responseEntity = new ResponseEntity("User Created" , HttpStatus.CREATED);
  }

  @GetMapping("/api/v1/userservice/users")
  public ResponseEntity getAllUsers(HttpServletRequest request){

    List<Customer> list =  userService.getAllCustomers();
    responseEntity = new ResponseEntity(list,HttpStatus.OK);
    return responseEntity;

  }



}
