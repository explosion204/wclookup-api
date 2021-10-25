package com.explosion204.wclookup.model.repository;

import com.explosion204.wclookup.model.entity.Review;
import com.explosion204.wclookup.model.entity.Toilet;
import com.explosion204.wclookup.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByUserAndToilet(User user, Toilet toilet);
}
