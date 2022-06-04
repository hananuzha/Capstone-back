package com.example.Reservation.Repository;


import com.example.Reservation.Entity.PropertyImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PropertyImageRepository extends JpaRepository<PropertyImage,Integer> {
    Optional<PropertyImage> findFirstById(Integer id);
}
