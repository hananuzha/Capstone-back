package com.example.Reservation.controller;

import com.example.Reservation.Entity.*;
import com.example.Reservation.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Pattern;
import java.util.List;

@RestController
@RequestMapping(path = "/internal")
@Validated
public class InternalController {

    public static final String POST_ADDRESS = "/address";
    public static final String GET_PROPERTY = "/property";
    public static final String POST_PROPERTY = "/property";
    public static final String GET_OWNER = "/owner";
    public static final String POST_OWNER = "/owner";
    public static final String GET_FEATURE = "/feature";
    public static final String POST_FEATURE = "/feature";
    private static final String GET_SCHEDULE = "/schedule";
    private static final String POST_SCHEDULE = "/schedule";
    private static final String POST_EXCEPTION = "/exception";
    private static final String PUT_RESERVATION_STATUS = "/reservation/status";
    private static final String PUT_RESERVATION_BUNDLE_STATUS = "/reservation/bundle/status";
    private static final String POST_PROPERTY_IMAGE = "/property/image";
    private static final String GET_PROPERTY_IMAGE = "/property/image/{propertyId}";
    private static final String POST_PROPERTY_IMAGE_PRIMARY = "/property/image/primary";



    @Autowired
    private AddressService addressService;
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private OwnerService ownerService;
    @Autowired
    private FeatureService featureService;
    @Autowired
    private PropertyScheduleService propertyScheduleService;
    @Autowired
    private ReservationService reservationService;

    @PostMapping(path = POST_ADDRESS)
    public Integer saveAddress(@RequestBody Address address) throws Exception {
        int id = addressService.saveAddress(address);
        return id;
    }

    @GetMapping(path = GET_PROPERTY)
    public List<Property> getPropertys(){
        List<Property> propertys = propertyService.getPropertys();
        return propertys;
    }

    @PostMapping(path = POST_PROPERTY)
    public int saveProperty(@RequestParam(name = "name") String name,
                            @RequestParam(name = "description") String description,
                            @RequestParam(name = "aliasName") String aliasName,
                            @RequestParam(name = "OwnerId") Integer OwnerId,
                            @RequestParam(name = "id",required = false) Integer id,
                            @RequestParam(name = "addressId",required = false) Integer addressId) throws Exception {
        try{
            int propertyId = propertyService.saveProperty(id,OwnerId,name,aliasName,description,addressId);
            return propertyId;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping(path = GET_OWNER)
    public List<Owner> getOwners(){
        List<Owner> owners = ownerService.getOwners();
        return owners;
    }

    @PostMapping(path = POST_OWNER)
    public Integer saveOwner(@RequestParam(name = "name") String name,
                            @RequestParam(name = "phone") String phone,
                            @RequestParam(name = "companyName") String companyName,
                            @RequestParam(name = "id",required = false) Integer id,
                            @RequestParam(name = "addressId",required = false) Integer addressId) throws Exception {
        try{
            int ownerId = ownerService.saveOwner(id,name,phone,companyName,addressId);
            return ownerId;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping(path = GET_FEATURE)
    public List<PropertyFeature> getFeature(@RequestParam(name = "propertyId") Integer propertyId){
        return featureService.getFeaturesByPropertyId(propertyId);
    }

    @PostMapping(path = POST_FEATURE)
    public Integer saveFeature(@RequestParam(name = "id",required = false) Integer id,
                              @RequestParam(name = "name") String name,
                              @RequestParam(name = "icon") String icon,
                              @RequestParam(name = "isCountable") String isCountable,
                              @RequestParam(name = "featureCount") Integer featureCount,
                              @RequestParam(name = "propertyId") Integer propertyId) throws Exception {
        return featureService.saveFeature(id,name,icon,isCountable,propertyId,featureCount);
    }

    @GetMapping(path = GET_SCHEDULE)
    public List<PropertySchedule> getSchedule(@RequestParam(name = "year")Integer year,
                                              @RequestParam(name = "month") Integer month,
                                              @RequestParam(name = "propertyId")Integer propertyId){
        try{
            return propertyScheduleService.getPropertySchedule(propertyId,month,year);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping(path = POST_SCHEDULE)
    public List<PropertySchedule> saveSchedule(@RequestParam(name = "fromDate")
                                               @Pattern(regexp = "^\\d{4}\\-\\d{2}\\-\\d{2}" , message = "Invalid date format")
                                                           String fromDate,
                             @RequestParam(name = "toDate")
                             @Pattern(regexp = "^\\d{4}\\-\\d{2}\\-\\d{2}" , message = "Invalid date format")String toDate,
                             @RequestParam(name = "day") Integer [] day,
                             @RequestParam(name = "propertyId")Integer propertyId,
                             @RequestParam(name = "price")Double price,
                             @RequestParam(name = "cost")Double cost) throws Exception {
        try{
            List<PropertySchedule> propertySchedules = propertyScheduleService
                    .addIntervalSchedule(fromDate,toDate,day,propertyId,price,cost);
            return propertySchedules;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping(path = POST_EXCEPTION)
    public PropertySchedule addException(@RequestParam(name = "propertyId")Integer propertyId,
                             @RequestParam(name = "reason")String reason,
                             @RequestParam(name = "exceptionDateString")
                             @Pattern(regexp = "^\\d{4}\\-\\d{2}\\-\\d{2}" , message = "Invalid date format")String exceptionDateString) throws Exception {
        try{
            PropertySchedule propertySchedule = propertyScheduleService
                    .addExceptionDate(propertyId,reason,exceptionDateString);
            return propertySchedule;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @PutMapping(path = PUT_RESERVATION_STATUS)
    public Integer changeReservationStatus(@RequestParam(name = "reservationId")Integer reservationId,
                                        @RequestParam(name = "status") Integer status) throws Exception {
        try{
            int reservationStatus = reservationService.changeReservationStatus(reservationId,status);
            return reservationStatus;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @PutMapping(path = PUT_RESERVATION_BUNDLE_STATUS)
    public Integer changeReservationBundleStatus(@RequestParam(name = "reservationBundleId")Integer reservationBundleId,
                                           @RequestParam(name = "status") Integer status) throws Exception {
        try{
            int reservationStatus = reservationService.changeReservationBundleStatus(reservationBundleId,status);
            return reservationStatus;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping(path = POST_PROPERTY_IMAGE)
    public String propertyImage(
            @RequestParam(name = "propertyId")Integer propertyId,
            @RequestParam(name = "propertyImageId",required = false)Integer propertyImageId,
            @RequestParam(name = "isPrimary")String primaryFlag,
            @RequestPart(name = "image")MultipartFile image
            ) throws Exception {
        try{
            String fileLocation = propertyService.addPropertyImage(propertyId,primaryFlag,image,propertyImageId);
            return fileLocation;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
    @GetMapping(path = GET_PROPERTY_IMAGE)
    public List<PropertyImage> propertyImageList(@PathVariable("propertyId") Integer propertyId) throws Exception {
        return propertyService.getPropertyImages(propertyId);
    }

}
