package com.ecommerce.ecommerceapi.dao;

import com.ecommerce.ecommerceapi.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail,Long> {
}
