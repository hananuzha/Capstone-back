package com.example.Reservation.Repository;

import com.example.Reservation.Entity.UserProfileToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserProfileTokenRepository extends JpaRepository<UserProfileToken, Integer> {

    List<UserProfileToken> findAllByUserProfileIdAndToken(Integer userProfileId, String token);

}
