package com.explosion204.wclookup.model.repository;

import com.explosion204.wclookup.model.entity.Toilet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ToiletRepository extends JpaRepository<Toilet, Long> {
    @Query(
        value = """
            SELECT id, address, schedule, latitude, longitude, rating, is_confirmed
            FROM toilet
            WHERE getDistance(latitude, longitude, :latitude, :longitude) < :radius AND is_confirmed = true
        """,
        countQuery = """
            SELECT COUNT(id)
            FROM toilet
            WHERE getDistance(latitude, longitude, :latitude, :longitude) < :radius AND is_confirmed = true
        """,
        nativeQuery = true
    )
    Page<Toilet> findByRadius(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("radius") int radius,
            Pageable pageable
    );

    @Query(value = "SELECT t FROM Toilet t WHERE t.isConfirmed = true")
    Page<Toilet> findAllConfirmed(Pageable pageable);
}
