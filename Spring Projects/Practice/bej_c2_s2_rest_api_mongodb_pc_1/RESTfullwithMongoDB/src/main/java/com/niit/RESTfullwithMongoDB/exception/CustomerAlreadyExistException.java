package com.niit.RESTfullwithMongoDB.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code= HttpStatus.CONFLICT,reason = "customer already exists")
public class CustomerAlreadyExistException extends Exception{

}
