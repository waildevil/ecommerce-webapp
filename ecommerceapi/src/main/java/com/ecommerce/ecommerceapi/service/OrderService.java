package com.ecommerce.ecommerceapi.service;

import com.ecommerce.ecommerceapi.dto.OrderCreationRequest;
import com.ecommerce.ecommerceapi.dto.OrderDTO;
import com.ecommerce.ecommerceapi.dto.OrderDetailDTO;
import com.ecommerce.ecommerceapi.entity.Order;
import com.stripe.exception.StripeException;

import java.util.List;

public interface OrderService {

    List<OrderDTO> findAll();
    Order findById(Long theId);
    String deleteOrder(Long orderId);
    Order createOrder(OrderDTO orderDTO);
    Order updateOrder(Long theId, OrderCreationRequest updateRequest);
    OrderDTO getOrderWithTotalPrice(Long orderId);
    Order createOrderFromCart(Long userId);
    Order updateOrderStatus(Long orderId, String status);
    List<OrderDTO> findAllOrdersSorted();

}
