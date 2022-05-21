package com.svalero.deliveryAPI.controller;

import com.svalero.deliveryAPI.domain.Order;
import com.svalero.deliveryAPI.domain.Restaurant;
import com.svalero.deliveryAPI.domain.Rider;
import com.svalero.deliveryAPI.exception.ErrorRespons;
import com.svalero.deliveryAPI.exception.OrderNotFoundException;
import com.svalero.deliveryAPI.exception.RiderNotFoundException;
import com.svalero.deliveryAPI.service.OrderService;
import com.svalero.deliveryAPI.service.RiderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RiderController {
    private final Logger logger = LoggerFactory.getLogger(RiderController.class);
    @Autowired
    private RiderService riderService;
    @Autowired
    private OrderService orderService;

    @GetMapping("/riders")
    public List<Rider> getAllRiders() {
        logger.info("Find Riders " );
        List<Rider> riders = riderService.findAll();
        logger.info("End Find Riders " );
        return riders;
    }
    @GetMapping("/rider/{id}")
    public Rider getRider(@PathVariable long id)throws RiderNotFoundException{
        logger.info("Find rider by id: " + id );
        Rider rider= riderService.findById(id);
        logger.info("End  rider by id: " + id );
        return rider;
    }
    @GetMapping("/rider")
    public List<Rider> getRiderByVehicle(@RequestParam(name = "vehicle", defaultValue = "")
                                                            String vehicle) {//?=
        List<Rider> riders;
        logger.info("Find rider by velocidad maxima: " + vehicle );
        if (vehicle.equals("")) {
            riders = riderService.findAll();
        } else {
            riders = riderService.findByVehicle(vehicle);
        }
        logger.info("End Find rider by vehicle: " + vehicle );
        return riders;
    }
    @DeleteMapping("/rider/{id}")
    public Rider removeRider(@PathVariable long id)throws RiderNotFoundException {
        logger.info("Delete rider id: " + id);
        Rider rider = riderService.deleteRider(id);
        logger.info("End Delete rider id:" + id );
        return rider;
    }
    @PostMapping("/riders")
    public Rider addRider(@RequestBody Rider rider) {//lo combierte a json
        logger.info("Add rider " );
        Rider newRider = riderService.addRider(rider);
        logger.info("End Add rider " );
        return newRider;
    }
    @PutMapping("/rider/{id}")
    public Rider modifyRider(@RequestBody Rider rider, @PathVariable long id)throws RiderNotFoundException {
        logger.info("Modify rider id:" + id );
        Rider newRider = riderService.modifyRider(id, rider);
        logger.info("End Modify rider id:" + id );
        return newRider;
    }
    @GetMapping("/rider/{riderId}/orders")//ordenes de un rider
    public List<Order> getOrders(@PathVariable long riderId,
                                 @RequestParam(name = "distance", defaultValue = "0")
                                         int distance) throws RiderNotFoundException {
        List<Order> orderList = null;
        logger.info("Find rider by id: " + riderId );
        Rider rider = riderService.findRider(riderId);

        if (distance != 0) {
            logger.info("Find rider by id2: " + riderId );
            orderList = orderService.findOrders(rider, distance);
        } else {
            logger.info("Find rider by id3: " + riderId );
            orderList = orderService.findOrders(rider);
            logger.info("Find rider by id4: " + riderId );
        }
        logger.info("End Find rider by id: " + riderId );
        return orderList;
    }
    @PatchMapping("/rider/{id}")
    public Rider patchRider(@PathVariable long id, @RequestBody String name) throws RiderNotFoundException {
        logger.info("Start PatchRider " + id);
        Rider rider = riderService.patchRider(id, name);
        logger.info("End patchRider " + id);
        return rider;
    }
    // Contar las orders de un usuario
    @GetMapping("/user/{id}/numOrders")
    public int numOrdersRider(@PathVariable long idRider) throws OrderNotFoundException, RiderNotFoundException, OrderNotFoundException {
        logger.info("Start numOrdersRider " + idRider);
        int users = orderService.numOrders(idRider);
        logger.info("End numOrdersRider " + idRider);
        return users;
    }
    @ExceptionHandler(RiderNotFoundException.class)
    public ResponseEntity<ErrorRespons> handleRiderNotFoundException(RiderNotFoundException rinfe){
        ErrorRespons errorRespons = new ErrorRespons("404", rinfe.getMessage());
        logger.info(rinfe.getMessage());
        return new ResponseEntity<>(errorRespons, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorRespons> handleException(Exception exception){
        ErrorRespons errorRespons = new ErrorRespons("999", "Internal Server error   ");
        return new ResponseEntity<>(errorRespons, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
