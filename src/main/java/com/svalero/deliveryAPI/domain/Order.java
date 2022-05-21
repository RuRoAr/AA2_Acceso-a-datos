package com.svalero.deliveryAPI.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "orders")
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column
    private long price;
    @Column
    private double weight;
    @Column
    private boolean ready;
    @Column
    private int time;
    @Column
    private int distance;

    @ManyToOne
    @JoinColumn(name = "rider_id")
    @JsonBackReference(value = "rider-order")//esto tambien hace que no
    // haga un bucle y por eso no devuleve en postman
    private Rider rider;
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    @JsonBackReference(value = "restaurant-order")
    private Restaurant restaurant;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "user-order")
    private User user;
}
