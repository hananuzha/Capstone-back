package com.example.Reservation.Repository;

import com.example.Reservation.Entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address,Integer> {

    Address findById(int id);

    @Modifying
    @Query(value = "update address a set a.street = :street, a.city = :city, a.buildingNumber = :buildingNumber, a.description = :description" +
            " where a.id = :id",nativeQuery = true)
    Integer updateAddress(@Param("id") Integer id, @Param("street") String street, @Param("city") String city, @Param("buildingNumber") String buildingNumber
            , @Param("description") String description);

}
