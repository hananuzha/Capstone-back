package com.example.Reservation.Repository;

import com.example.Reservation.Entity.PropertyFeature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeatureRepository extends JpaRepository<PropertyFeature,Integer> {

    @Modifying
    @Query(value = "update property_feature a set a.name = :name, a.count = :count, a.icon = :icon, a.is_countable = :is_countable, a.property_id = :property_id" +
            " where a.id = :id",nativeQuery = true)
    Integer updateFeature(@Param("id") Integer id, @Param("property_id") Integer property_id, @Param("name") String name, @Param("count") Integer count, @Param("icon") String icon
            , @Param("is_countable") String is_countable);
    List<PropertyFeature> findByPropertyId(Integer id);
}