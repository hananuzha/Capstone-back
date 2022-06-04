package com.example.Reservation.service;

import com.example.Reservation.Entity.Address;
import com.example.Reservation.Repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddressService {

    @Autowired
    AddressRepository addressRepository;

    @Transactional
    public int saveAddress(Address address) throws Exception {
        //??
        try{
            if(address.getId()!=null){
                addressRepository.findById(address.getId()).orElseThrow(() -> new Exception("Invalid Address Id."));
            }
            addressRepository.save(address);
            return address.getId();
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
}
