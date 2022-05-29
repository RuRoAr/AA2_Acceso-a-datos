package com.svalero.deliveryAPI.controller;


import com.svalero.deliveryAPI.domain.Order;
import com.svalero.deliveryAPI.domain.Restaurant;
import com.svalero.deliveryAPI.exception.ErrorRespons;
import com.svalero.deliveryAPI.exception.OrderNotFoundException;
import com.svalero.deliveryAPI.exception.RestaurantNotFoundException;
import com.svalero.deliveryAPI.service.OrderService;
import com.svalero.deliveryAPI.service.RestaurantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RestaurantController {

    private final Logger logger = LoggerFactory.getLogger(RestaurantController.class);
    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private OrderService orderService;
    @GetMapping("/restaurants")
    public ResponseEntity<List<Restaurant>> getAllRestaurants(){
        logger.info("Start getRestaurant");
        List<Restaurant> restaurants = restaurantService.findAllRestaurants();
        logger.info("End getRestaurant");
        return ResponseEntity.ok(restaurants);
    }
    @GetMapping("/restaurant/{id}")
    public ResponseEntity <Restaurant> getRestaurant(@PathVariable long id)throws RestaurantNotFoundException{
        logger.info("Start getRestaurant: " + id);
        Restaurant restaurant= restaurantService.findRestaurant(id);
        logger.info("End getRestaurant: " + id);
        return ResponseEntity.ok(restaurant);
    }
    @GetMapping("/restaurant")
    public ResponseEntity<List<Restaurant>> getRestaurantByCategory( @RequestParam(name = "category", defaultValue = "0") String category) {//?=
        List<Restaurant> restaurants;
        logger.info("Found Restaurant: " + category );
        if (category.equals("0")) {
            restaurants = restaurantService.findAllRestaurants();
        } else {
            restaurants = restaurantService.findByCategory(category);
        }
        logger.info("End Found Restaurant: " + category );
        return ResponseEntity.ok(restaurants);
    }

    @DeleteMapping("/restaurant/{id}")
    public ResponseEntity<Restaurant> removeRestaurant(@PathVariable long id)throws RestaurantNotFoundException {
        logger.info("Delete Restaurant: " + id );
        Restaurant restaurant = restaurantService.deleteRestaurant(id);
        logger.info("End delete Restaurant: " + id );
        return ResponseEntity.ok(restaurant);
    }
    @PostMapping("/restaurants")
    public ResponseEntity<?> addRestaurant(@RequestBody Restaurant restaurant) {//lo combierte a json
       // ResponseEntity
        if(restaurant.getName() == null)
            return ResponseEntity.badRequest().body(ErrorRespons.mandatoryField("El nomnbre es un campo obligatorio"));
        if(restaurant.getAddress() == null)
            return ResponseEntity.badRequest().body(ErrorRespons.mandatoryField("La direccion es un campo obligatorio"));
        if(restaurant.getCapacity() == 0)
            return ResponseEntity.badRequest().body(ErrorRespons.mandatoryField("La capacidad es un campo obligatorio"));
        if(restaurant.getMediumPrice() == 0)
            return ResponseEntity.badRequest().body(ErrorRespons.mandatoryField("El precio medio es un campo obligatorio"));
        if(restaurant.getCategory() == null)
            return ResponseEntity.badRequest().body(ErrorRespons.mandatoryField("La categoria es un campo obligatorio"));
        logger.info("Add Restaurant "  );
        Restaurant newRestaurant = restaurantService.addRestaurant(restaurant);
        logger.info("End Add Restaurant " );
        return ResponseEntity.ok(newRestaurant);
    }
    @PutMapping("/restaurant/{id}")
    public ResponseEntity<Restaurant> modifyRestaurant(@RequestBody Restaurant restaurant, @PathVariable long id)throws  RestaurantNotFoundException {
        logger.info("Modify Restaurant: " + id );
        Restaurant newRestaurant = restaurantService.modifyRestaurant(id, restaurant);
        logger.info("End Modify Restaurant: " + id );
        return ResponseEntity.ok(newRestaurant);
    }

    @PatchMapping("/restaurant/{id}")
    public ResponseEntity<Restaurant> patchRestaurant(@PathVariable long id, @RequestBody boolean operative) throws RestaurantNotFoundException {
        logger.info("Start PatchRestaurant " + id);
        Restaurant restaurant = restaurantService.patchRestaurant(id, operative);
        logger.info("End patchRider " + id);
        return ResponseEntity.ok(restaurant);
    }
    @ExceptionHandler(RestaurantNotFoundException.class)
    public ResponseEntity<ErrorRespons> handleRestaurantNotFoundException(RestaurantNotFoundException rnfe){
        ErrorRespons errorRespons = new ErrorRespons(404, rnfe.getMessage());
        logger.info(rnfe.getMessage());
        return new ResponseEntity<>(errorRespons, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler
    public ResponseEntity<ErrorRespons> handleException(Exception exception){
        ErrorRespons errorRespons = new ErrorRespons(999, "Internal Server error   ");
        return new ResponseEntity<>(errorRespons, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    }
