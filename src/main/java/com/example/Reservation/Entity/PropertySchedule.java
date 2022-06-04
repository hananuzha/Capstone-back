package com.example.Reservation.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="property_schedule")
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PropertySchedule {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private String reason;

    @Column(name = "is_reserved")
    private String isReserved;

    @Column(name = "is_exception")
    private String isException;

    private Double price;

    private Double cost;

    @Column(name = "scheduled_date")
    @Temporal(TemporalType.DATE)
    private Date scheduledDate;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "property_id")
    @JsonIgnore
    private Property property;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "reservation_id")
        private Reservation reservation;
}
