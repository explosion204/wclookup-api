package com.explosion204.wclookup.model.repository;

import com.explosion204.wclookup.model.entity.Toilet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ToiletRepository extends JpaRepository<Toilet, Long> {
    @Query(
        value = """
            SELECT id, address, schedule, latitude, longitude, rating, confirmed
            FROM toilet
            WHERE getDistance(latitude, longitude, :latitude, :longitude) < :radius AND confirmed = :confirmed
        """,
        countQuery = """
            SELECT COUNT(id)
            FROM toilet
            WHERE getDistance(latitude, longitude, :latitude, :longitude) < :radius AND confirmed = :confirmed
        """,
        nativeQuery = true
    )
    Page<Toilet> findByRadius(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("radius") double radius,
            @Param("confirmed") boolean confirmed,
            Pageable pageable
    );

    @Query(value = "SELECT t FROM Toilet t WHERE t.confirmed = :confirmed")
    Page<Toilet> findAll(boolean confirmed, Pageable pageable);

    @Query(value = "SELECT t FROM Toilet t WHERE t.id = :id AND t.confirmed = true")
    Optional<Toilet> findByIdConfirmed(@Param("id") long id);
}
