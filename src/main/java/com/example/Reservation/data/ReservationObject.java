package com.example.Reservation.data;


import com.example.Reservation.Entity.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReservationObject {
    String userName;
    Address address;
    String paymentMethod;
    String note;

}
