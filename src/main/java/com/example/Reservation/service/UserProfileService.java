package com.example.Reservation.service;

import com.example.Reservation.Entity.UserProfile;
import com.example.Reservation.Repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @PersistenceContext
    private EntityManager entityManager;
    public UserProfile login(String phoneNumber, String password) throws Exception {
        List data = entityManager.createNativeQuery("select * from user_profile  "
                + "where phone = ? and password = ?", UserProfile.class).setParameter(1, phoneNumber).setParameter(2, password).getResultList();
        if (data.isEmpty()) {
            return null;
        }
        return (UserProfile) data.get(0);
    }
}
