package com.explosion204.wclookup.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "toilet")
public class Toilet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String address;
    private String schedule;
    private double latitude;
    private double longitude;
    private double rating;
    private boolean isConfirmed;
}
