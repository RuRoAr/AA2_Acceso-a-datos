package com.svalero.deliveryAPI.controller;


import com.svalero.deliveryAPI.domain.Order;
import com.svalero.deliveryAPI.domain.Restaurant;
import com.svalero.deliveryAPI.domain.Rider;
import com.svalero.deliveryAPI.domain.dto.OrderDto;
import com.svalero.deliveryAPI.exception.*;
import com.svalero.deliveryAPI.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {

    private final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @GetMapping("/orders")
    public List<Order> getAllOrders() {
        logger.info("Get Orders" );
        List<Order> orders = orderService.findAll();
        logger.info("End Get Orders" );
        return orders;
    }

    @GetMapping("/order/{id}")
    public Order getOrder(@PathVariable long id) throws OrderNotFoundException{
        logger.info("Get Order for id: " + id );
        Order order= orderService.findOrder(id);
        logger.info("End Get Order for id: " + id );
        return order;
    }
    @GetMapping("/orders")
    public List<Order> getOrderByDistance(@RequestParam(name = "distance", defaultValue = "")int distance) {//?=
        List<Order> orders;
        logger.info("Get Order for time: " + distance );
        if (distance != 0) {
            orders = orderService.findByDistance(distance);
        } else {
            orders = orderService.findAll();
        }
        logger.info("End Get Order for time: " + distance );
        return orders;
    }
    @DeleteMapping("/order/{id}")
    public Order removeOrder(@PathVariable long id) throws OrderNotFoundException {
        logger.info("Delete Order: " + id );
        Order order = orderService.deleteOrder(id);
        logger.info("End Delete Order: " + id );
        return order;
    }
//    @PostMapping("/orders")
//    public Order addOrder(@RequestBody Order order) {//lo combierte a json
//        Order newOrder = orderService.addOrder(order);
//        return newOrder;
//    }
    @PostMapping("/orders")
    public Order addOrder(@RequestBody OrderDto orderDto)throws UserNotFoundException,
            RestaurantNotFoundException, RiderNotFoundException {//lo combierte a json
//        Order newOrder = orderService.addOrder(orderDto);
//        return newOrder;
        logger.info("Add Order: " );
        return orderService.addOrder(orderDto);
    }

    @PutMapping("/order/{id}")
    public Order modifyOrder(@RequestBody Order order, @PathVariable long id)throws OrderNotFoundException {
        logger.info("Modify Order: " + id );
        Order newOrder = orderService.modifyOrder(id, order);
        logger.info("End Modify Order: " + id );
        return newOrder;
    }
    @PatchMapping("/order/{id}")//cambiar el estado de un pedido
    public Order patchOrder(@PathVariable long id, @RequestBody boolean ready) throws OrderNotFoundException {
        logger.info("Start PatchOrder " + id);
        Order order = orderService.patchOrder(id, ready);
        logger.info("End patchRider " + id);
        return order;
    }
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorRespons> handleOrderNotFoundException(OrderNotFoundException ornfe){
        ErrorRespons errorRespons = new ErrorRespons("404", ornfe.getMessage());
        logger.info(ornfe.getMessage());
        return new ResponseEntity<>(errorRespons, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler
    public ResponseEntity<ErrorRespons> handleException(Exception exception){
        ErrorRespons errorRespons = new ErrorRespons("999", "Internal Server error   ");
        return new ResponseEntity<>(errorRespons, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
