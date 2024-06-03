package com.ecommerce.ecommerceapi.dto;

import com.ecommerce.ecommerceapi.entity.Order;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private Long id;
    private Date date;
    private String status;
    private Long userId;
    private double totalPrice;
    private List<OrderDetailDTO> orderDetails = new ArrayList<>();

    public OrderDTO(Order order) {
        this.id = order.getId();
        this.date = order.getDate();
        this.status = order.getStatus();
        this.userId = order.getUser() != null ? order.getUser().getId() : null;
        this.totalPrice = order.getTotalPrice();
        this.orderDetails = order.getOrderDetails().stream()
                .map(OrderDetailDTO::new)
                .collect(Collectors.toList());
    }
}
