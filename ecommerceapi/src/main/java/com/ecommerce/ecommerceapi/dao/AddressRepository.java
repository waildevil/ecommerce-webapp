package com.ecommerce.ecommerceapi.dao;

import com.ecommerce.ecommerceapi.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address,Long> {
}
