package com.ecommerce.ecommerceapi.service;

import com.ecommerce.ecommerceapi.dao.AddressRepository;
import com.ecommerce.ecommerceapi.dao.UserRepository;
import com.ecommerce.ecommerceapi.dto.AddressDTO;
import com.ecommerce.ecommerceapi.entity.*;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService{

    private static final Logger logger = LoggerFactory.getLogger(AddressService.class);
    private AddressRepository addressRepository;

    private UserRepository userRepository;

    public AddressServiceImpl(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    @Override
    public AddressDTO getAddressById(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Address not found with ID: " + id));
        return new AddressDTO(address);
    }

    @Override
    public Address findById(Long theId) {
        Optional<Address> result = addressRepository.findById(theId);

        Address theAddress = null;
        if(result.isPresent()){
            theAddress = result.get();
        }
        else{
            throw new RuntimeException("Did not find Address id - " + theId);
        }
        return theAddress;
    }



    @Transactional
    public Address createAddress(AddressDTO addressDTO) {
        logger.info("Creating new address with details: {}", addressDTO);
        User user = userRepository.findById(addressDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + addressDTO.getUserId()));

        Address address = new Address();
        address.setUser(user);
        address.setName(addressDTO.getName());
        address.setAddressLine1(addressDTO.getAddressLine1());
        address.setAddressLine2(addressDTO.getAddressLine2());
        address.setState(addressDTO.getState());
        address.setCountry(addressDTO.getCountry());
        address.setCity(addressDTO.getCity());
        address.setZipCode(addressDTO.getZipCode());
        address.setPhoneNumber(addressDTO.getPhoneNumber());

        return addressRepository.save(address);
    }

    @Override
    public Address updateAddress(Long theId, AddressDTO addressDTO) {

        Address existingAddress = addressRepository.findById(theId)
                .orElseThrow(() -> new IllegalArgumentException("Address not found with ID: " + theId));

        existingAddress.setName(addressDTO.getName());
        existingAddress.setAddressLine1(addressDTO.getAddressLine1());
        existingAddress.setAddressLine2(addressDTO.getAddressLine2());
        existingAddress.setState(addressDTO.getState());
        existingAddress.setCountry(addressDTO.getCountry());
        existingAddress.setZipCode(addressDTO.getZipCode());
        existingAddress.setCity(addressDTO.getCity());
        existingAddress.setPhoneNumber(addressDTO.getPhoneNumber());

        return addressRepository.save(existingAddress);
    }

    @Override
    public String deleteAddress(Long addressId) {

        Address addressToDelete = addressRepository.findById(addressId)
                .orElseThrow(() -> new IllegalArgumentException("Address not found with ID: " + addressId));
        addressRepository.delete(addressToDelete);
        return "Address with ID " + addressId + " has been deleted successfully.";
    }


}
