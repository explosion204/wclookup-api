package com.explosion204.wclookup.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Data
@Entity
@Table(name = "toilet")
public class Toilet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address;
    private String schedule;
    private double latitude;
    private double longitude;
    private double rating;
    private boolean confirmed;

    @OneToMany(mappedBy = "toilet")
    private List<Review> reviews;
}
