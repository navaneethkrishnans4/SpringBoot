package com.niit.JWTauthPractice.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Customer {

  @Id
  @GeneratedValue
  private int customerId;
  private String customerName;
  private String password;
  private Long phno;

  public Customer(){}

  public String toString() {
    return "User{" +
            "userId=" + customerId +
            ", username='" + customerName + '\'' +
            ", password='" + password + '\'' +
            ", phno='" + phno + '\'' +
            '}';
  }

  public int getCustomerId() {
    return customerId;
  }

  public void setCustomerId(int customerId) {
    this.customerId = customerId;
  }

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Long getPhno() {
    return phno;
  }

  public void setPhno(Long phno) {
    this.phno = phno;
  }

  public Customer(int customerId, String customerName, String password, Long phno) {
    this.customerId = customerId;
    this.customerName = customerName;
    this.password = password;
    this.phno = phno;
  }




}
