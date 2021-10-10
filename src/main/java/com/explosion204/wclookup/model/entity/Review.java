package com.explosion204.wclookup.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "toilet_id", referencedColumnName = "id")
    private Toilet toilet;

    private double rating;
    private String text;
}
