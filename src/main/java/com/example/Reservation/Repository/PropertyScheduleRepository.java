package com.example.Reservation.Repository;

import com.example.Reservation.Entity.PropertySchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface PropertyScheduleRepository extends JpaRepository<PropertySchedule,Integer> {
    PropertySchedule findByPropertyIdAndScheduledDate(Integer id, Date date);
}
