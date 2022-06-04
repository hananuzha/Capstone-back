/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.Reservation.controller;


import com.example.Reservation.Entity.Property;
import com.example.Reservation.Entity.PropertySchedule;
import com.example.Reservation.data.ReservationObject;
import com.example.Reservation.service.PropertyScheduleService;
import com.example.Reservation.service.PropertyService;
import com.example.Reservation.service.ReservationService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(path = "/external")
@Validated
public class ExternalController {
    
    private static final String GET_PROPERTY = "/property";
    private static final String GET_PROPERTY_BY_ID = "/property/{id}";
    private static final String POST_RESERVATION = "/reservation/";
    private static final String GET_SCHEDULE = "/schedule";
    
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private PropertyScheduleService propertyScheduleService;



    @GetMapping("/property")
    public List<Property> getPropertys(){
        List<Property> properties = propertyService.getPropertys();
        return  properties;
    }

    @GetMapping(path = GET_PROPERTY_BY_ID)
    public Property getPropertyById(@PathVariable(value = "id") @Pattern(regexp = "`^\\d+$`",message = "not number") @NonNull @Min(value = 1,message = "minimum is 1") Integer id){
        Property property = propertyService.getPropertyById(id);
        return property;
    }



    @PostMapping(path = POST_RESERVATION)
    public String postReservation(@RequestParam(name = "fromDate")
                                  @Pattern(regexp = "^\\d{4}\\-\\d{2}\\-\\d{2}" , message = "Invalid date format")
                                  String fromDate,
                                  @RequestParam(name = "toDate")
                                  @Pattern(regexp = "^\\d{4}\\-\\d{2}\\-\\d{2}" , message = "Invalid date format")
                                  String toDate,
                                  @RequestParam(name = "propertyId")Integer propertyId,
                                  @RequestParam(name = "phoneNumber")String phoneNumber,
                                  @RequestBody ReservationObject reservationObject ) throws Exception {
        try{
            boolean flag = reservationService.reserveProperty(fromDate,toDate,reservationObject,propertyId,phoneNumber);
            if(flag)
                return "Reservation completed successfully";
            return "Something went wrong. Please try again later!";
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
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




}
