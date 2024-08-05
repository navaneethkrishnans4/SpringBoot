package com.niit.JWTauthPractice.service;

import com.niit.JWTauthPractice.domain.Customer;

import java.util.Map;

public interface SecurityTokenGenerator {

  Map<String,String> generateToken(Customer user);
}
