package com.explosion204.wclookup.model.repository;

import com.explosion204.wclookup.model.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
