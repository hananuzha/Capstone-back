package com.example.Reservation.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
@Entity
@Table(name="reservation")
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Reservation {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private String status;

    private double price;

    @Column(name = "reservation_date")
    private Date reservationDate;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "reservation_bundle_id")
    @JsonIgnore
    private ReservationBundle reservationBundle;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "property_schedule_id")
    private PropertySchedule propertySchedule;
}
