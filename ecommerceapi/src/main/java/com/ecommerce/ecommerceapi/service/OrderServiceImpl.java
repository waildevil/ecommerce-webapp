package com.ecommerce.ecommerceapi.service;

import com.ecommerce.ecommerceapi.dao.CartRepository;
import com.ecommerce.ecommerceapi.dao.OrderRepository;
import com.ecommerce.ecommerceapi.dao.ProductRepository;
import com.ecommerce.ecommerceapi.dao.UserRepository;
import com.ecommerce.ecommerceapi.dto.OrderCreationRequest;
import com.ecommerce.ecommerceapi.dto.OrderDTO;
import com.ecommerce.ecommerceapi.dto.OrderDetailDTO;
import com.ecommerce.ecommerceapi.entity.*;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService{


    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private CartService cartService;
    private CartItemService cartItemService;
    private ProductRepository productRepository;


    @Autowired
    public OrderServiceImpl(OrderRepository theOrderRepository, UserRepository theUserRepository,
                            ProductRepository theProductRepository, CartService theCartService,
                            CartItemService theCartItemService) {
        orderRepository = theOrderRepository;
        userRepository = theUserRepository;
        productRepository = theProductRepository;
        cartService = theCartService;
        cartItemService = theCartItemService;

    }


    @Override
    public List<OrderDTO> findAll() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public Order findById(Long theId) {
        Optional<Order> result = orderRepository.findById(theId);

        Order theOrder = null;
        if(result.isPresent()){
            theOrder = result.get();
        }
        else{
            throw new RuntimeException("Did not find order id - " + theId);
        }
        return theOrder;
    }

    @Transactional
    public Order createOrder(OrderDTO orderDTO) {
        double totalPrice = 0.0;
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + orderDTO.getUserId()));

        Order order = new Order();
        order.setUser(user);
        order.setDate(new Date());
        order.setStatus(orderDTO.getStatus());

        List<OrderDetail> orderDetailsList = new ArrayList<>();

        for (OrderDetailDTO detailDTO : orderDTO.getOrderDetails()) {
            Product product = productRepository.findById(detailDTO.getProduct().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + detailDTO.getProduct().getId()));

            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(product);
            detail.setQuantity(detailDTO.getQuantity());
            orderDetailsList.add(detail);
            totalPrice += (product.getPrice() * detailDTO.getQuantity());

        }
        order.setTotalPrice(totalPrice);
        order.setOrderDetails(orderDetailsList);
        return orderRepository.save(order);
    }

    @Override
    public Order updateOrder(Long orderId, OrderCreationRequest updateRequest) {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

        existingOrder.setStatus(updateRequest.getStatus());

        Map<Long, OrderDetail> detailMap = existingOrder.getOrderDetails()
                .stream()
                .collect(Collectors.toMap(
                        detail -> detail.getProduct().getId(),
                        detail -> detail));

        for (OrderDetailDTO detailDTO : updateRequest.getOrderDetails()) {
            Product product = productRepository.findById(detailDTO.getProduct().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + detailDTO.getProduct().getId()));

            OrderDetail detail = detailMap.computeIfAbsent(detailDTO.getProduct().getId(), k -> new OrderDetail());
            detail.setOrder(existingOrder);
            detail.setProduct(product);
            detail.setQuantity(detailDTO.getQuantity());

            if (detail.getId() == null) {
                existingOrder.getOrderDetails().add(detail);
            }
        }
        existingOrder.getOrderDetails().removeIf(detail -> !detailMap.containsKey(detail.getProduct().getId()));

        return orderRepository.save(existingOrder);
    }

    @Override
    public OrderDTO getOrderWithTotalPrice(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

        OrderDTO orderDTO = new OrderDTO(order);
        return orderDTO;
    }

    @Transactional
    public String deleteOrder(Long orderId) {
        Order orderToDelete = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

        orderRepository.delete(orderToDelete);
        return "Order with ID " + orderId + " has been deleted successfully.";
    }

    @Transactional
    public Order createOrderFromCart(Long userId) {
        Cart cart = cartService.getCartEntityByUserId(userId);
        if (cart == null || cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cannot create order from an empty cart.");
        }


        for (CartItem item : cart.getItems()) {
            Optional<Product> productOpt = productRepository.findById(item.getProduct().getId());
            if (productOpt.isEmpty()) {
                throw new RuntimeException("Product not found with id: " + item.getProduct().getId());
            }
        }

        Order order = new Order();
        order.setUser(cart.getUser());
        List<OrderDetail> orderDetails = cart.getItems().stream().map(cartItem -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(cartItem.getProduct());
            orderDetail.setQuantity(cartItem.getQuantity());
            return orderDetail;
        }).collect(Collectors.toList());

        order.setOrderDetails(orderDetails);
        Order savedOrder = orderRepository.save(order);

        System.out.println("Order Details: " + orderDetails);
        System.out.println("Total Price: " + order.getTotalPrice());

        cartService.clearCart(userId);

        return savedOrder;
    }

    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

        order.setStatus(status);
        return orderRepository.save(order);
    }

    @Override
    public List<OrderDTO> findAllOrdersSorted() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderDTO::new)
                .sorted(Comparator.comparing(OrderDTO::getDate).reversed())
                .collect(Collectors.toList());
    }




}
