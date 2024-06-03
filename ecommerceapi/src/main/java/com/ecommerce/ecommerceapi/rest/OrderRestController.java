package com.ecommerce.ecommerceapi.rest;

import com.ecommerce.ecommerceapi.config.JwtService;
import com.ecommerce.ecommerceapi.dao.OrderRepository;
import com.ecommerce.ecommerceapi.dto.OrderCreationRequest;
import com.ecommerce.ecommerceapi.dto.OrderDTO;
import com.ecommerce.ecommerceapi.dto.OrderRequest;
import com.ecommerce.ecommerceapi.entity.Category;
import com.ecommerce.ecommerceapi.entity.Order;
import com.ecommerce.ecommerceapi.service.OrderService;
import com.paypal.api.payments.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    private OrderService orderService;
    private JwtService jwtService;

    @Autowired
    public OrderRestController(OrderService theOrderService, JwtService theJwtService){
        orderService = theOrderService;
        jwtService = theJwtService;
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orders = orderService.findAllOrdersSorted();
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/createOrder")
    public ResponseEntity<?> createOrder(@RequestBody OrderDTO orderDTO) {
        try {
            Order order = orderService.createOrder(orderDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Order created with id - " + order.getId());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/createOrderFromCart")
    public ResponseEntity<Order> createOrderFromCart(@RequestHeader("Authorization") String token) {
        Long userId = jwtService.extractUserId(token.substring(7));
        Order order = orderService.createOrderFromCart(userId);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/updateOrder/{orderId}")
    public ResponseEntity<?> updateOrder(@PathVariable Long orderId, @RequestBody OrderCreationRequest request) {
        try {
            Order updatedOrder = orderService.updateOrder(orderId, request);
            return ResponseEntity.ok(updatedOrder);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error updating order: " + e.getMessage());
        }
    }


    @DeleteMapping("/deleteOrder/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long orderId) {
        try {
            String resultMessage = orderService.deleteOrder(orderId);
            return ResponseEntity.ok(resultMessage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error deleting order: " + e.getMessage());
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderWithTotalPrice(@PathVariable Long orderId) {
        try {
            OrderDTO orderDTO = orderService.getOrderWithTotalPrice(orderId);
            return ResponseEntity.ok(orderDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }




}
