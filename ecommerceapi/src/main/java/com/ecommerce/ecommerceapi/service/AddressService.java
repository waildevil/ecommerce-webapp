package com.ecommerce.ecommerceapi.service;

import com.ecommerce.ecommerceapi.dto.AddressDTO;
import com.ecommerce.ecommerceapi.entity.Address;

public interface AddressService {

    Address createAddress(AddressDTO addressDTO);
    AddressDTO getAddressById(Long id);
    Address findById(Long theId);
    Address updateAddress(Long theId, AddressDTO addressDTO);
    String deleteAddress(Long addressId);

}
