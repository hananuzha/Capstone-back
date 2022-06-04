package com.example.Reservation.service;

import com.example.Reservation.Entity.Address;
import com.example.Reservation.Entity.Owner;
import com.example.Reservation.Repository.AddressRepository;
import com.example.Reservation.Repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OwnerService {

    @Autowired
    OwnerRepository ownerRepository;
    @Autowired
    AddressRepository addressRepository;

    public List<Owner> getOwners(){
        try{
            List<Owner> owners = ownerRepository.findAll();
            owners.stream().forEach(p -> p.setPropertys(null));
            return owners;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Transactional
    public int saveOwner(Integer id,String name, String phone, String companyName, Integer addressId) throws Exception {
        try{
            Address address = null;
            if(addressId!=null){
                address = addressRepository.findById(addressId).orElseThrow(() -> new Exception("Invalid address Id"));
            }
            Owner owner;
            if(id!=null){
                int count = ownerRepository.updateOwner(id,name,phone,companyName,addressId);
                if(count==0){
                    throw new Exception("Update operation cannot be done. Owner id is not found");
                }
                return id;
            }else{
                owner = new Owner(null,name,phone,companyName,address,null);
                ownerRepository.save(owner);
                return owner.getId();
            }
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public Integer saveAddress(Address address) throws Exception {
        //??
        try{
            if(address!=null && address.getId()!=null){
                int count = addressRepository.updateAddress(address.getId(),address.getStreet(),address.getCity(),address.getBuildingNumber(),address.getDescription());
                if(count==0){
                    throw new Exception("Update operation cannot be done. Address id is not found");
                }
                return address.getId();
            }else{
                addressRepository.save(address);
                return address.getId();
            }
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
}
