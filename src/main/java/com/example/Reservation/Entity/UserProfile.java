package com.example.Reservation.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="user_profile")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserProfile {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String phone;

    @OneToMany(mappedBy = "userProfile",orphanRemoval = true)
    @JsonIgnoreProperties("userProfile")
    private List<ReservationBundle> reservationBundles;
}
