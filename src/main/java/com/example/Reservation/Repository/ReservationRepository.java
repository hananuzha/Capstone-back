package com.example.Reservation.Repository;

import com.example.Reservation.Entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Integer> {
    Optional<Reservation> findById(Integer id);
    List<Reservation> findAllByReservationBundleId(Integer id);
}
