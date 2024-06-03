package com.ecommerce.ecommerceapi.service;

import com.ecommerce.ecommerceapi.dao.OrderDetailRepository;
import com.ecommerce.ecommerceapi.dto.OrderDetailDTO;
import com.ecommerce.ecommerceapi.entity.Order;
import com.ecommerce.ecommerceapi.entity.OrderDetail;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderDetailServiceImpl implements OrderDetailService{


    private OrderDetailRepository orderDetailRepository;

    @Autowired
    public OrderDetailServiceImpl(OrderDetailRepository theOrderDetailRepository){
        orderDetailRepository = theOrderDetailRepository;
    }

    @Override
    public List<OrderDetail> findAll() {
        return orderDetailRepository.findAll();
    }

    @Override
    public OrderDetailDTO findById(Long theId) {
        OrderDetail orderDetail = orderDetailRepository.findById(theId)
                .orElseThrow(() -> new RuntimeException("Did not find OrderDetail id - " + theId));

        if (orderDetail.getProduct() == null) {
            throw new RuntimeException("Product data not found for OrderDetail with id: " + theId);
        }

        return new OrderDetailDTO(orderDetail);
    }

    @Override
    public OrderDetail save(OrderDetail theOrderDetail) {
        return orderDetailRepository.save(theOrderDetail);
    }

    @Override
    public void deleteById(Long theId) {
        orderDetailRepository.deleteById(theId);
    }

    @Override
    public OrderDetail updateOrderDetail(Long theId, OrderDetail theOrderDetail) {
        OrderDetail orderDetailToUpdate = orderDetailRepository.findById(theId).
                orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + theId));
        orderDetailToUpdate.setQuantity(theOrderDetail.getQuantity());

        return orderDetailRepository.save(orderDetailToUpdate);
    }
}
