package com.svalero.deliveryAPI.controller;


import com.svalero.deliveryAPI.domain.Order;
import com.svalero.deliveryAPI.domain.Restaurant;
import com.svalero.deliveryAPI.domain.Rider;
import com.svalero.deliveryAPI.domain.User;
import com.svalero.deliveryAPI.exception.ErrorRespons;
import com.svalero.deliveryAPI.exception.RiderNotFoundException;
import com.svalero.deliveryAPI.exception.UserNotFoundException;
import com.svalero.deliveryAPI.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        logger.info("Find Users " );
        List<User> users = userService.findAll();
        logger.info("End Find Users " );
        return users;
    }
    @GetMapping("/user/{id}")
    public User getUser(@PathVariable long id)throws UserNotFoundException{
        logger.info("Find Users by id:" + id );
        User user= userService.findById(id);
        logger.info("End Find Users by id:" + id );
        return user;
    }
    @GetMapping("/user")
    public List<User> getUserBySurname(@RequestParam(name = "Surname", defaultValue = "")
                                                            String surname) {//?=
        logger.info("Find Users by surname:" + surname );
        List<User> users;
        if (surname.equals("")) {
            users= userService.findAll();
        } else {
            users = userService.findBySurname(surname);
        }
        logger.info("End Find Users by surname:" + surname );
        return users;
    }
    @DeleteMapping("/user/{id}")
    public User removeUser(@PathVariable long id)throws UserNotFoundException {
        logger.info("Delete Users by id:" + id );
        User user = userService.deleteUser(id);
        logger.info("End Delete Users by id:" + id );
        return user;
    }
    @PostMapping("/users")
    public User addUser(@RequestBody User user) {//lo combierte a json
        logger.info("Add Users by id:" );
        User newUser = userService.addUser(user);
        logger.info("End Add Users by id:" );
        return newUser;
    }
    @PutMapping("/user/{id}")
    public User modifyUser(@RequestBody User user, @PathVariable long id)throws UserNotFoundException {
        logger.info("Modify Users by id:" + id );
        User newUser = userService.modifyUser(id, user);
        logger.info("End Modify Users by id:" + id );
        return newUser;
    }
    @PatchMapping("/user/{id}")
    public User patchUser(@PathVariable long id, @RequestBody String address) throws UserNotFoundException {
        logger.info("Start PatchUser " + id);
        User user = userService.patchUser(id, address);
        logger.info("End patchUser " + id);
        return user;
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorRespons> handleUserNotFoundException(UserNotFoundException unfe){
        ErrorRespons errorRespons = new ErrorRespons("404", unfe.getMessage());
        logger.info(unfe.getMessage());
        return new ResponseEntity<>(errorRespons, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorRespons> handleException(Exception exception){
        ErrorRespons errorRespons = new ErrorRespons("999", "Internal Server error   ");
        return new ResponseEntity<>(errorRespons, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
