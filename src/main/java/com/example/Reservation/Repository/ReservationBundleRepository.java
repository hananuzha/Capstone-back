package com.example.Reservation.Repository;

import com.example.Reservation.Entity.ReservationBundle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationBundleRepository extends JpaRepository<ReservationBundle,Integer> {
    List<ReservationBundle> findAllByFromDateBetweenAndUntilDateBetween(Date fromDate, Date untilDate, Date fromDate2, Date untilDate2);
    Optional<ReservationBundle> findById(Integer id);
}
