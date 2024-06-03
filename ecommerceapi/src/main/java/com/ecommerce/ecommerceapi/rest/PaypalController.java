package com.ecommerce.ecommerceapi.rest;

import com.ecommerce.ecommerceapi.config.JwtService;
import com.ecommerce.ecommerceapi.dao.ProductRepository;
import com.ecommerce.ecommerceapi.dto.CartDTO;
import com.ecommerce.ecommerceapi.dto.CartItemDTO;
import com.ecommerce.ecommerceapi.dto.OrderDTO;
import com.ecommerce.ecommerceapi.dto.OrderDetailDTO;
import com.ecommerce.ecommerceapi.dto.ProductDTO;
import com.ecommerce.ecommerceapi.entity.Order;
import com.ecommerce.ecommerceapi.entity.Product;
import com.ecommerce.ecommerceapi.entity.User;
import com.ecommerce.ecommerceapi.service.CartService;
import com.ecommerce.ecommerceapi.service.OrderService;
import com.ecommerce.ecommerceapi.service.PaypalService;
import com.ecommerce.ecommerceapi.service.UserService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/paypal")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PaypalController {

    private final PaypalService paypalService;
    private final OrderService orderService;
    private final UserService userService;
    private final JwtService jwtService;
    private final CartService cartService;
    @Autowired
    private ProductRepository productRepository;

    @Value("${paypal.success.url}")
    private String successUrl;

    @Value("${paypal.cancel.url}")
    private String cancelUrl;

    @PostMapping("/pay")
    public ResponseEntity<String> pay(@RequestParam("sum") double sum,
                                      @RequestParam(value = "productId", required = false) Long productId,
                                      @RequestParam(value = "quantity", required = false) Integer quantity,
                                      @RequestBody(required = false) List<CartItemDTO> cartItems,
                                      @RequestParam("jwt") String jwt) {
        try {
            String description;
            if (cartItems != null && !cartItems.isEmpty()) {
                StringBuilder descriptionBuilder = new StringBuilder("Payment for items: ");
                for (CartItemDTO item : cartItems) {
                    descriptionBuilder.append(item.getProduct().getName())
                            .append(" (x").append(item.getQuantity()).append("), ");
                }
                description = descriptionBuilder.toString();
            } else if (productId != null && quantity != null) {

                Product product = productRepository.findById(productId)
                        .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));
                description = "Payment for " + product.getName() + " (x" + quantity + ")";
            } else {
                return new ResponseEntity<>("Missing parameters for payment", HttpStatus.BAD_REQUEST);
            }


            String successUrlWithParams = successUrl + "?jwt=" + jwt;
            if (productId != null) {
                successUrlWithParams += "&productId=" + productId;
            }
            if (quantity != null) {
                successUrlWithParams += "&quantity=" + quantity;
            }

            Payment payment = paypalService.createPayment(
                    sum,
                    "EUR",
                    "paypal",
                    "sale",
                    description,
                    cancelUrl,
                    successUrlWithParams);

            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    return new ResponseEntity<>(link.getHref(), HttpStatus.OK);
                }
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/success")
    @Transactional
    public ResponseEntity<String> successPay(@RequestParam("paymentId") String paymentId,
                                             @RequestParam("PayerID") String payerId,
                                             @RequestParam("jwt") String jwt,
                                             @RequestParam(value = "productId", required = false) Long productId,
                                             @RequestParam(value = "quantity", required = false) Integer quantity) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                System.out.println("Payment approved with Payment ID: " + paymentId);

                Long userId = jwtService.extractUserId(jwt);
                if (userId == null) {
                    System.out.println("Invalid token: user ID not found");
                    return new ResponseEntity<>("Invalid token: user ID not found", HttpStatus.UNAUTHORIZED);
                }
                System.out.println("User ID extracted from JWT: " + userId);

                Order order;
                if (productId != null && quantity != null) {
                    System.out.println("Creating order for single product ID: " + productId + " with quantity: " + quantity);
                    order = createOrderAfterPayment(payment, userId, productId, quantity);
                } else {
                    System.out.println("Retrieving cart for user ID: " + userId);
                    CartDTO cartDTO = cartService.getCartByUserId(userId);
                    if (cartDTO == null || cartDTO.getItems().isEmpty()) {
                        System.out.println("Cart is empty or not found for user ID: " + userId);
                        return new ResponseEntity<>("Cart is empty or not found", HttpStatus.BAD_REQUEST);
                    }
                    System.out.println("Cart retrieved with " + cartDTO.getItems().size() + " items.");
                    order = createOrderAfterPayment(payment, userId, cartDTO.getItems());
                }

                System.out.println("Order created with ID: " + order.getId() + ", Total Price: " + order.getTotalPrice());
                return new ResponseEntity<>("Order created.", HttpStatus.OK);
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @GetMapping("/cancel")
    public ResponseEntity<String> cancelPay() {
        return new ResponseEntity<>("Cancelled", HttpStatus.OK);
    }

    private Order createOrderAfterPayment(Payment payment, Long userId, Long productId, int quantity) {

        User user = userService.getUserEntityById(userId);

        double amount = Double.parseDouble(payment.getTransactions().get(0).getAmount().getTotal());


        List<OrderDetailDTO> orderDetails = new ArrayList<>();
        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
        orderDetailDTO.setProduct(new ProductDTO(productId));
        orderDetailDTO.setQuantity(quantity);
        orderDetails.add(orderDetailDTO);


        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId(user.getId());
        orderDTO.setTotalPrice(amount);
        orderDTO.setDate(new Date());
        orderDTO.setStatus("PAID");
        orderDTO.setOrderDetails(orderDetails);


        return orderService.createOrder(orderDTO);
    }

    private Order createOrderAfterPayment(Payment payment, Long userId, List<CartItemDTO> cartItems) {
        System.out.println("Creating order after payment");

        User user = userService.getUserEntityById(userId);
        if (user == null) {
            System.out.println("User not found with ID: " + userId);
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
        System.out.println("User found: " + user.getUsername());

        double amount = Double.parseDouble(payment.getTransactions().get(0).getAmount().getTotal());
        System.out.println("Payment amount: " + amount);

        if (cartItems == null || cartItems.isEmpty()) {
            System.out.println("Cart items are empty");
            throw new IllegalArgumentException("Cart items are empty");
        } else {
            System.out.println("Total cart items: " + cartItems.size());
        }

        List<OrderDetailDTO> orderDetails = new ArrayList<>();
        for (CartItemDTO cartItem : cartItems) {
            if (cartItem.getProduct() == null) {
                System.out.println("Product is null for cart item");
                continue;
            }
            OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
            orderDetailDTO.setProduct(cartItem.getProduct());
            orderDetailDTO.setQuantity(cartItem.getQuantity());
            orderDetails.add(orderDetailDTO);
            System.out.println("Added order detail: Product ID - " + cartItem.getProduct().getId() + ", Quantity - " + cartItem.getQuantity());
        }

        if (orderDetails.isEmpty()) {
            System.out.println("No order details found, something went wrong.");
        } else {
            System.out.println("Total order details added: " + orderDetails.size());
        }

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId(user.getId());
        orderDTO.setTotalPrice(amount);
        orderDTO.setDate(new Date());
        orderDTO.setStatus("PAID");
        orderDTO.setOrderDetails(orderDetails);

        System.out.println("OrderDTO created: User ID - " + orderDTO.getUserId() + ", Total Price - " + orderDTO.getTotalPrice());

        Order order = orderService.createOrder(orderDTO);
        System.out.println("Order created with ID: " + order.getId() + ", Total Price: " + order.getTotalPrice());

        return order;
    }


}
