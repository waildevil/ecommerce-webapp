package com.ecommerce.ecommerceapi.service;


import com.ecommerce.ecommerceapi.dto.OrderDetailDTO;
import com.ecommerce.ecommerceapi.entity.OrderDetail;

import java.util.List;

public interface OrderDetailService {

    List<OrderDetail> findAll();
    OrderDetailDTO findById(Long theId);
    OrderDetail save(OrderDetail theOrderDetail);
    void deleteById(Long theId);
    OrderDetail updateOrderDetail(Long theId, OrderDetail theOrderDetail);
}
