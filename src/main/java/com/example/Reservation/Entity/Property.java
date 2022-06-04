package com.example.Reservation.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="property")
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Property {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(name = "alias_name")
    private String aliasName;

    private String description;

    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id")
    @JsonIgnore
    private Owner owner;

    @OneToMany(mappedBy = "property",orphanRemoval = true)
    private List<PropertyFeature> propertyFeatures;

    @OneToMany(mappedBy = "property",orphanRemoval = true)
    private List<PropertySchedule> propertySchedules;

    @OneToMany(mappedBy = "property",orphanRemoval = true)
    private List<ReservationBundle> reservationBundles;

    @OneToMany(mappedBy = "property",orphanRemoval = true)
    private List<PropertyImage> propertyImages;
}
