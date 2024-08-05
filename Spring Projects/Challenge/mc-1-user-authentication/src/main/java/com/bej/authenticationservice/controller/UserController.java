package com.bej.authenticationservice.controller;


import com.bej.authenticationservice.domain.User;
import com.bej.authenticationservice.exception.UserAlreadyExistException;
import com.bej.authenticationservice.service.SecurityTokenGenerator;
import com.bej.authenticationservice.service.UserService;
import com.bej.authenticationservice.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


@RestController
public class UserController {

  /*
   * Autowiring should be implemented for the UserService and SecurityTokenGenerator. (Use Constructor-based
   * autowiring) Please note that we should not create an object using the new
   * keyword
   */


  // first step - register the User
  private ResponseEntity responseEntity;
    private UserService userService;
    private SecurityTokenGenerator securityTokenGenerator;


    @Autowired
    public UserController(UserService userService , SecurityTokenGenerator securityTokenGenerator)
    {
        this.userService = userService;
        this.securityTokenGenerator = securityTokenGenerator;
    }
    @PostMapping("/register")
    public ResponseEntity saveUser(@RequestBody User user) throws UserAlreadyExistException {

        User createdUser = userService.saveUser(user);
        return responseEntity = new ResponseEntity("User Created" , HttpStatus.CREATED);
    }
  /*
   * Define a handler method which will save a specific User by reading the
   *  object from request body and save the User details in the
   * database. This handler method should return any one of the status messages
   * basis on different situations:
   * 1. 201(CREATED) - If the user saved successfully.
   * 2. 500(INTERNAL_SERVER_ERROR) - If the User already exists
   *
   * This handler method should map to the URL "/register" using HTTP POST method
   */


  // second step - login the customer
  @PostMapping("/login")
  public ResponseEntity loginUser(@RequestBody User user) throws UserNotFoundException {

      Map<String, String> map = null;
      try {
          User userObj = userService.findByUsernameAndPassword(user.getUsername(), user.getPassword());
          if (userObj.getUsername().equals(user.getUsername())) {
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

  /* Define a handler method which will authenticate a User by reading the User
   * object from request body containing the username and password. The username and password should be validated
   * before proceeding ahead with JWT token generation. The User credentials will be validated against the database entries.
   * The error should be return if validation is not successful. If credentials are validated successfully, then JWT
   * token will be generated. The token should be returned back to the caller along with the API response.
   * This handler method should return any one of the status messages basis on different
   * situations:
   * 1. 200(OK) - If login is successful
   * 2. 401(UNAUTHORIZED) - If login is not successful
   *
   * This handler method should map to the URL "/login" using HTTP POST method
   */



//     Define a handler method that will get all  User from the database the
//     handler mapping url will be "/api/v1/userservice/users"
//    Status will be
//    1. 200(OK) - If all Users are successfully retrieved
//    2. 500(INTERNAL_SERVER_ERROR) - If any other error occurs
@GetMapping("/api/v1/userservice/users")
public ResponseEntity getAllUsers(HttpServletRequest request){

    List<User> list =  userService.getAllUsers();
    responseEntity = new ResponseEntity(list,HttpStatus.OK);
    return responseEntity;

}

  //    Define a handler method that will delete a  User
  //    the handler mapping url will be "/api/v1/userservice/{userId}"
//    Status will be
//    1. 200(OK) - If delete is successful
//    2. 500(INTERNAL_SERVER_ERROR) - If any other error occurs


    @DeleteMapping("/api/v1/userservice/{userId}")
    public ResponseEntity deleteUser(@PathVariable int userId) {
        try {
            boolean deleted = userService.deleteUser(userId);
            if (deleted) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }


}
