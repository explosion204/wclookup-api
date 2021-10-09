package com.explosion204.wclookup.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String googleId;
    private String nickname;
    private String refreshToken;
    private LocalDateTime refreshTokenExpiration;
    private boolean isAdmin;
}
