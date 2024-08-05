package com.niit.RESTfullwithMongoDB.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(
        code = HttpStatus.NOT_FOUND,
        reason = "customer id doesn't exists"
)
public class CustomerNotFoundException extends Exception{
    public CustomerNotFoundException() {
        System.out.println("Customer Not Found!");
    }
}
