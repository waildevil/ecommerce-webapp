package com.ecommerce.ecommerceapi.dao;

import com.ecommerce.ecommerceapi.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
}
