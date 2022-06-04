/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.Reservation.service;

import com.example.Reservation.Entity.*;
import com.example.Reservation.Repository.AddressRepository;
import com.example.Reservation.Repository.OwnerRepository;
import com.example.Reservation.Repository.PropertyImageRepository;
import com.example.Reservation.Repository.PropertyRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PropertyService {
    
    @Value("${commonFeatures}")
    private String commonFeaturesString;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PropertyRepository propertyRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private PropertyImageRepository propertyImageRepository;
    
    public List<Property> getPropertys(){
        try{
            List<Property> properties = propertyRepository.findAll();
            properties.stream().forEach(p ->
                    p.setPropertyFeatures(
                            p.getPropertyFeatures().stream().filter(
                                    feature ->
                            feature.getName().toLowerCase().matches(commonFeaturesString.toLowerCase())
                                                                )
                            .collect(Collectors.toList())
                                            )

                                    );
            properties.stream().forEach(p -> {
                p.setPropertySchedules(null);
                p.setReservationBundles(null);
                //p.setPropertyFeatures(null);
            });
            System.out.println("size is" + properties.get(0).getPropertyFeatures().size());
            return properties;
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }
    
    public Property getPropertyById(Integer id){
        try{
            Property property = propertyRepository.findById(id).orElse(null);
            List<PropertySchedule> propertySchedules = entityManager.createNativeQuery("select sch.* from property_schedule sch " +
                    "where property_id = ? and MONTH(scheduled_date) = MONTH(CURRENT_DATE()) ",PropertySchedule.class).setParameter(1,id).getResultList();
            property.setPropertySchedules(propertySchedules);
            property.setReservationBundles(null);
            return property;
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }


    @Transactional
    public Integer saveProperty(Integer id, Integer ownerId, String name, String aliasName, String description, Integer addressId) throws Exception {
        try{
            Address address = null;
            if(addressId!=null){
                address = addressRepository.findById(addressId).orElseThrow(() -> new Exception("Invalid address Id"));
            }
            Property property;
            if(id!=null){
                int count = propertyRepository.updateProperty(id,name,aliasName,description,addressId,ownerId);
                if(count==0){
                    throw new Exception("Update operation cannot be done. Proerty id is not found");
                }
                return id;
            }else{
                Owner owner = ownerRepository.findById(ownerId).orElseThrow(() -> new Exception("Operation cannot be done. Owner id object is not found"));
                property = new Property();
                property.setName(name);
                property.setAddress(address);
                property.setOwner(owner);
                property.setAliasName(aliasName);
                property.setDescription(description);
                propertyRepository.save(property);
                return property.getId();
            }
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
//Multipart upload allows you to upload a single object as a set of parts.
    @Transactional
    public String addPropertyImage(Integer propertyId, String primaryFlag, MultipartFile multipartFile,Integer peropertyImageId) throws Exception {
        try{
            String fileExtension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
            Property property = propertyRepository.findById(propertyId).orElseThrow(() -> new Exception("Invalid Property ID"));
            PropertyImage propertyImage;
            if(peropertyImageId!=null){
                propertyImage = propertyImageRepository.findFirstById(peropertyImageId).orElseThrow(() -> new Exception("Invalid propertyImageID"));
                propertyImage.setIsPrimary(primaryFlag);
                propertyImage.setFileExt(fileExtension);
                propertyImageRepository.save(propertyImage);
            }else{
                propertyImage = new PropertyImage();
                propertyImage.setFileExt(fileExtension);
                propertyImage.setIsPrimary(primaryFlag);
                propertyImage.setProperty(property);
                propertyImageRepository.save(propertyImage);
            }
            File dir = new File("propertyImages");
            if (!dir.isDirectory()) {
                dir.mkdir();
            }
            //stream means continuous flow of data.
            OutputStream os = new FileOutputStream(new File("/Users/hanaalnuzhah/Desktop/App/FirstAngulor/src/assets/img/" + propertyImage.getId() + "." + fileExtension));
            os.write(multipartFile.getBytes());
            os.close();
            return "/Users/hanaalnuzhah/Desktop/App/FirstAngulor/src/assets/img/" + propertyImage.getId() + "." + fileExtension;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
    public List<PropertyImage> getPropertyImages(Integer propertyId) throws Exception {
        try {
            return propertyRepository.findById(propertyId).get().getPropertyImages();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

}
