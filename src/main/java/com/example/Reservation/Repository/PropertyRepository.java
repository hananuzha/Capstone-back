/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.Reservation.Repository;

import com.example.Reservation.Entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property,Integer> {
    List<Property> findAll(); 
    Property findById(int id);
    //select property that not reserved with the date
    @Query(value = "select p from Property p inner join p.propertySchedules as sch "
            + "where p.id = :id and sch.isReserved = 'N' ")
    List<Property> findByIdAndRangeDates(@Param("id") Integer id);

//    @Query(value = "select property from Property property inner join proper ty.propertySchedules as sch" +
//            " inner join property.propertyFeatures as fea "
//            + "where sch.price >= :fromPrice and sch.price <= :toPrice" +
//            " and fea.name in :features ")
//    List<Property> searchProperty(@Param("fromPrice") Double fromPrice,@Param("toPrice") Double toPrice,
//                                  @Param("features") Collection<String> features);
    @Modifying
    @Query(value = "update property p set p.name = :name, p.alias_name = :aliasName, p.owner_id = :owner_id, p.description = :description, p.address_id = :addressId" +
            " where p.id = :id",nativeQuery = true)
    Integer updateProperty(@Param("id") Integer id,@Param("name") String name,@Param("aliasName") String aliasName,@Param("description") String description
            ,@Param("addressId") Integer addressId,@Param("owner_id") Integer owner_id);
}
