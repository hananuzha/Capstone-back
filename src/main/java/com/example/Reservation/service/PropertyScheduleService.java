package com.example.Reservation.service;

import com.example.Reservation.Entity.Property;
import com.example.Reservation.Entity.PropertySchedule;
import com.example.Reservation.Repository.PropertyRepository;
import com.example.Reservation.Repository.PropertyScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.joda.time.DateTime;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormat;



@Service
public class PropertyScheduleService {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private PropertyScheduleRepository propertyScheduleRepository;
    @Autowired
    private PropertyRepository propertyRepository;





//now if i want to get schedule of that property in specific month of year
    public List<PropertySchedule> getPropertySchedule(int propertyId, int monthNumber, int yearNumber){
        try{
            //???
            List<PropertySchedule> propertySchedules = entityManager.createNativeQuery("select sch.* from property_schedule sch " +
                    "where property_id = ? and MONTH(scheduled_date) = ? and YEAR(scheduled_date) = ? ",PropertySchedule.class).setParameter(1,propertyId)
                    .setParameter(2,monthNumber).setParameter(3,yearNumber)
                    .getResultList();
            propertySchedules.stream().forEach(p -> {
                p.setReservation(null);
                p.setProperty(null);
            });
            return propertySchedules;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
//now if i want to add a new scedual  of that property that is available during that period
    @Transactional
    public List<PropertySchedule> addIntervalSchedule(String fromDate, String toDate, Integer [] day,Integer propertyId,
                                    double price,double cost) throws Exception {
        try{
            List<PropertySchedule> propertySchedules = new ArrayList<>();
            Property property = propertyRepository.findById(propertyId).orElseThrow(() -> new Exception("Invalid property ID"));
            List<DateTime> dates = calculateDayDates(fromDate,toDate,day);
            for(DateTime dateTime : dates){
                //get schedule in that date
                PropertySchedule propertySchedule = propertyScheduleRepository.findByPropertyIdAndScheduledDate(propertyId,dateTime.toDate());
                //already exist but available
                if(propertySchedule !=null){
                    //it is not reserved and there is no exception so i can edit the price and cost
                    if(!propertySchedule.getIsReserved().equalsIgnoreCase("Y")
                            && !propertySchedule.getIsException().equalsIgnoreCase("Y")){
                        propertySchedule.setPrice(price);
                        propertySchedule.setCost(cost);
                        propertyScheduleRepository.save(propertySchedule);
                        propertySchedules.add(propertySchedule);
                    }
                }else{
                    //it is not exist
                    propertySchedule = new PropertySchedule();
                    propertySchedule.setCost(cost);
                    propertySchedule.setProperty(property);
                    propertySchedule.setPrice(price);
                    propertySchedule.setReservation(null);
                    propertySchedule.setIsReserved("N");
                    propertySchedule.setIsException("N");
                    propertySchedule.setScheduledDate(dateTime.toDate());
                    propertyScheduleRepository.save(propertySchedule);
                    propertySchedules.add(propertySchedule);
                }
            }
            return propertySchedules;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
//i can use calender
    public List<DateTime> calculateDayDates(String fromDate, String toDate, Integer [] day){
        DateTimeFormatter pattern = DateTimeFormat.forPattern("yyyy-MM-dd");
        DateTime startDate = pattern.parseDateTime(fromDate);
        DateTime endDate = pattern.parseDateTime(toDate);
        List<DateTime> dates = new ArrayList<>();
        int i=0;
        while (startDate.isBefore(endDate)){
            if ( startDate.getDayOfWeek() == day[i] ){
                dates.add(startDate);
            }
            startDate = startDate.plusDays(1);
            i++;
        }
        return dates;
    }

    @Transactional
    public PropertySchedule addExceptionDate(int propertyId, String reason, String exceptionDateString) throws Exception {
        try{
            DateTimeFormatter pattern = DateTimeFormat.forPattern("yyyy-MM-dd");
            DateTime exceptionDate = pattern.parseDateTime(exceptionDateString);
            Property property = propertyRepository.findById(propertyId);
            if(property ==null)
                throw new Exception("Invalid property ID");
            PropertySchedule propertySchedule = propertyScheduleRepository.findByPropertyIdAndScheduledDate(propertyId,exceptionDate.toDate());
            if(propertySchedule != null){
                if(propertySchedule.getIsReserved().equalsIgnoreCase("Y")){
                    throw new Exception("This date is reserved already. Action cannot be done on this date. Please contact your IT manager!");
                }else{
                    propertySchedule.setIsException("Y");
                    propertySchedule.setReason(reason);
                    propertyScheduleRepository.save(propertySchedule);
                }
            }else{
                propertySchedule = new PropertySchedule();
                propertySchedule.setScheduledDate(exceptionDate.toDate());
                propertySchedule.setIsReserved("N");
                propertySchedule.setIsException("Y");
                propertySchedule.setReason(reason);
                propertySchedule.setProperty(property);
                propertyScheduleRepository.save(propertySchedule);
            }
            return propertySchedule;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }


}
