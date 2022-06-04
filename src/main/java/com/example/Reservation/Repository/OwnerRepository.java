package com.example.Reservation.Repository;

import com.example.Reservation.Entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OwnerRepository extends JpaRepository<Owner,Integer> {
    List<Owner> findAll();

    Owner findById(int id);

    @Modifying
    @Query(value = "update owner o set o.name = :name, o.phone = :phone, o.companyName = :companyName, o.address_id = :addressId" +
            " where o.id = :id",nativeQuery = true)
    Integer updateOwner(@Param("id") Integer id, @Param("name") String name, @Param("phone") String phone, @Param("companyName") String companyName
            , @Param("addressId") Integer addressId);
}
