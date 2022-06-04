package com.example.Reservation.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="reservation_bundle")
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ReservationBundle {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private String status;

    private double price;

    private String note;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "from_date")
    private Date fromDate;

    @Column(name = "until_date")
    private Date untilDate;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "property_id")
    @JsonIgnore
    private Property property;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "user_profile_id")
    @JsonIgnore
    private UserProfile userProfile;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "reservationBundle",orphanRemoval = true,cascade = CascadeType.ALL)
    private List<Reservation> reservations;

}
