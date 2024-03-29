package com.svalero.deliveryAPI.controller;


import com.svalero.deliveryAPI.domain.Order;
import com.svalero.deliveryAPI.domain.Restaurant;
import com.svalero.deliveryAPI.domain.Rider;
import com.svalero.deliveryAPI.domain.User;
import com.svalero.deliveryAPI.exception.*;
import com.svalero.deliveryAPI.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        logger.info("Find Users " );
        List<User> users = userService.findAll();
        logger.info("End Find Users " );
        return ResponseEntity.ok(users);
    }
    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUser(@PathVariable long id)throws UserNotFoundException{
        logger.info("Find Users by id:" + id );
        User user= userService.findById(id);
        logger.info("End Find Users by id:" + id );
        return ResponseEntity.ok(user);
    }
    @GetMapping("/user")
    public ResponseEntity<List<User>> getUserBySurname(@RequestParam(name = "Surname", defaultValue = "")
                                                            String surname) {//?=
        logger.info("Find Users by surname:" + surname );
        List<User> users;
        if (surname.equals("")) {
            users= userService.findAll();
        } else {
            users = userService.findBySurname(surname);
        }
        logger.info("End Find Users by surname:" + surname );
        return ResponseEntity.ok(users);
    }
    @DeleteMapping("/user/{id}")
    public ResponseEntity<User> removeUser(@PathVariable long id)throws UserNotFoundException {
        logger.info("Delete Users by id:" + id );
        User user = userService.deleteUser(id);
        logger.info("End Delete Users by id:" + id );
        return ResponseEntity.ok(user);
    }
    @PostMapping("/users")
    public ResponseEntity<User> addUser(@RequestBody User user) {//lo combierte a json
        logger.info("Add Users by id:" );
        User newUser = userService.addUser(user);
        logger.info("End Add Users by id:" );
        return ResponseEntity.ok(newUser);
    }
    @PutMapping("/user/{id}")
    public ResponseEntity<User> modifyUser(@RequestBody User user, @PathVariable long id)throws UserNotFoundException {
        logger.info("Modify Users by id:" + id );
        User newUser = userService.modifyUser(id, user);
        logger.info("End Modify Users by id:" + id );
        return ResponseEntity.ok(newUser);
    }
    @PatchMapping("/user/{id}")
    public ResponseEntity<User> patchUser(@PathVariable long id, @RequestBody String address) throws UserNotFoundException {
        logger.info("Start PatchUser " + id);
        User user = userService.patchUser(id, address);
        logger.info("End patchUser " + id);
        return ResponseEntity.ok(user);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException manve) {
        logger.info("400: Argument not valid");
        Map<String, String> errors = new HashMap<>();
        manve.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        return ResponseEntity.badRequest().body(ErrorResponse.validationError(errors));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException bre) {
        logger.info("400: Bad request");
        return ResponseEntity.badRequest().body(ErrorResponse.badRequest(bre.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCountryNotFoundException(UserNotFoundException cnfe) {
        logger.info("404: Order not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.resourceNotFound(cnfe.getMessage()));
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ErrorResponse> handleInternalServerErrorException(InternalServerErrorException isee) {
        logger.info("500: Internal server error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse.internalServerError(isee.getMessage()));
    }
}
