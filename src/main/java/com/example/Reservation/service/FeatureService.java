package com.example.Reservation.service;

import com.example.Reservation.Entity.Property;
import com.example.Reservation.Entity.PropertyFeature;
import com.example.Reservation.Repository.FeatureRepository;
import com.example.Reservation.Repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FeatureService {

    @Autowired
    FeatureRepository featureRepositry;
    @Autowired
    PropertyRepository propertyRepository;

    @Transactional
    public Integer saveFeature(Integer id,String name,String icon,String isCountable,Integer propertyId, Integer featureCount) throws Exception {
        try{
             if(id!=null){
                int count = featureRepositry.updateFeature(id,propertyId,name,featureCount,icon,isCountable);
                if(count==0){
                    throw new Exception("Update operation cannot be done. Feature id is not found");
                }
                return id;

            }//save it, it is a new feature
            else{
                 Property property=null;
                 if(propertyId !=null)
                     property = propertyRepository.findById(propertyId).orElseThrow(() -> new Exception("Operation cannot be done due to invalid propertyId"));
                 PropertyFeature propertyFeature = new PropertyFeature();
                propertyFeature.setCount(featureCount);
                propertyFeature.setIcon(icon);
                propertyFeature.setIsCountable(isCountable);
                propertyFeature.setName(name);
                propertyFeature.setProperty(property);
                featureRepositry.save(propertyFeature);
                return propertyFeature.getId();
            }
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public List<PropertyFeature> getFeaturesByPropertyId(Integer propertyId){
        try{
            List<PropertyFeature> propertyFeatures = featureRepositry.findByPropertyId(propertyId);
            System.out.println("size is" + propertyFeatures.size());
            return propertyFeatures;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
}
