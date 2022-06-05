package com.example.Reservation.service;

import com.example.Reservation.Entity.*;
import com.example.Reservation.Repository.*;

import com.example.Reservation.data.ReservationObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class ReservationService {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    PropertyRepository propertyRepository;
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    ReservationBundleRepository reservationBundleRepository;
    @Autowired
    UserProfileRepository userProfileRepository;
    @Autowired
    PropertyScheduleRepository propertyScheduleRepository;

    @Transactional
    public boolean reserveProperty(String fromDateString, String toDateString,
                                   ReservationObject reservationObject, Integer propertyId, String phoneNumber) throws Exception {
        try{
            Date fromDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(fromDateString + " 00:00:00");
            Date toDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(toDateString + " 00:00:00");
            Property property = propertyRepository.findById(Integer.valueOf(propertyId)).orElse(null);
            if(!Optional.ofNullable(property).isPresent()){
                throw new Exception("Property Id invalid");
            }
            Query query = entityManager.createNativeQuery("select count(*) from property_schedule r where" +
                    " r.is_reserved = 'N' and r.is_exception = 'N' and " +
                    " r.property_id = " + propertyId + " and " +
                    "DATE_FORMAT(r.scheduled_date, '%Y-%m-%d 00:00:00') between '" + fromDateString + " 00:00:00' and '" + toDateString + " 23:59:59'");
            //vonvert from millseconds to days
            //getTime() method returns the number of milliseconds
            //check if property available during that period
            long difference = TimeUnit.DAYS.convert(toDate.getTime() - fromDate.getTime(),TimeUnit.MILLISECONDS) + 1;
            Integer count = ((Number)query.getSingleResult()).intValue();
            if(count<difference){
                throw new Exception("Selected date has some conflicts. Please choose different date and try again!");
            }
            UserProfile userProfile = userProfileRepository.findByPhone(phoneNumber);
            if(!Optional.ofNullable(userProfile).isPresent()){

                userProfile = new UserProfile();
                userProfile.setName(reservationObject.getUserName());
                userProfile.setPhone(phoneNumber);

                userProfileRepository.save(userProfile);
            }
            double sum = 0;
            //now i have to get scheduale information for each day to create reservation for each day
            List<Reservation> reservations = processReservations(fromDateString,toDateString,propertyId);
            for(Reservation reservation : reservations){
                sum = sum + reservation.getPrice();
            }
            ReservationBundle reservationBundle = new ReservationBundle();
            reservationBundle.setStatus("2");
            reservationBundle.setPrice(sum);
            reservationBundle.setFromDate(fromDate);
            reservationBundle.setUntilDate(toDate);
            reservationBundle.setUserProfile(userProfile);
            reservationBundle.setPaymentMethod(reservationObject.getPaymentMethod());
            reservationBundle.setNote(reservationObject.getNote());
            reservationBundleRepository.save(reservationBundle);
            reservations.stream().forEach(p -> p.setReservationBundle(reservationBundle));
            reservationRepository.saveAll(reservations);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public List<Reservation> processReservations(String fromDateString, String toDateString, Integer propertyId) throws ParseException {
        List<Reservation> reservations = new ArrayList<>();
        Date fromDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(fromDateString + " 00:00:00");
        Date toDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(toDateString + " 00:00:00");
        //??+1 cleaning day
        long difference = TimeUnit.DAYS.convert(toDate.getTime() - fromDate.getTime(),TimeUnit.MILLISECONDS) + 1;
        if(difference == 1){
            PropertySchedule propertySchedule = propertyScheduleRepository.findByPropertyIdAndScheduledDate(propertyId,fromDate);
            propertySchedule.setIsReserved("Y");
            propertyScheduleRepository.save(propertySchedule);
            Reservation reservation = new Reservation(null,"2",propertySchedule.getPrice(),fromDate,null,propertySchedule);
            reservations.add(reservation);
        }else{
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(fromDate);
            for(int x=0;x<(int)difference;x++){
                System.out.println();
                PropertySchedule propertySchedule = propertyScheduleRepository.findByPropertyIdAndScheduledDate(propertyId,calendar.getTime());
                Reservation reservation = new Reservation();
                reservation.setStatus("2");
                reservation.setReservationDate(calendar.getTime());
                reservation.setPrice(propertySchedule.getPrice());
                reservation.setPropertySchedule(propertySchedule);
                propertySchedule.setIsReserved("Y");
                propertySchedule.setReservation(reservation);
                propertyScheduleRepository.save(propertySchedule);
                reservations.add(propertySchedule.getReservation());
                calendar.add(Calendar.DAY_OF_WEEK,1);
            }
        }
        return reservations;
    }

    public List<ReservationBundle> getReservationBundle(String fromDateString,String toDateString) throws ParseException {
        Date fromDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(fromDateString + " 00:00:00");
        Date toDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(toDateString + " 00:00:00");
        List<ReservationBundle> reservationBundles = reservationBundleRepository.findAllByFromDateBetweenAndUntilDateBetween(fromDate,toDate,fromDate,toDate);
        return reservationBundles;
    }

    // Status -> 1 pending , 2 confirmed , 3 paid , 4 completed , 5 rejected
    @Transactional
    public int changeReservationStatus(Integer reservationId,int status) throws Exception {
        try{
            Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new Exception("Invalid reservation ID"));
            reservation.setStatus(String.valueOf(status));
            reservationRepository.save(reservation);
            return status;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    // Status -> 1 pending , 2 confirmed , 3 paid , 4 completed , 5 rejected
    @Transactional
    public int changeReservationBundleStatus(Integer reservationBundleId, int status) throws Exception {
        try{
            ReservationBundle reservationBundle = reservationBundleRepository.findById(reservationBundleId).orElseThrow
                    (() -> new Exception("Invalid Reservation bundle ID"));
            reservationBundle.setStatus(String.valueOf(status));
            reservationBundleRepository.save(reservationBundle);
            return status;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

}
