package com.ecommerce.ecommerceapi.dao;

import com.ecommerce.ecommerceapi.entity.Order;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {

}
