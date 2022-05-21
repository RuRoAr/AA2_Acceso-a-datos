package com.svalero.deliveryAPI.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "restaurants")
@Table(name = "restaurants")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column
    private String name;
    @Column
    private String address;
    @Column
    private int capacity;
    @Column
    private  boolean operative;
    @Column(name = "medium_price")
    private float mediumPrice;
    @Column
    private String category;

    @OneToMany(mappedBy = "restaurant")
    private List<Order> orders;

}
