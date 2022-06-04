/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.Reservation.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "user_profile_tokens")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserProfileToken implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_profile_id")
    private Integer userProfileId;

    @Column(name = "token")
    private String token;

    @Column(name = "created_at")
    private Date createdAt = new Date();
}
