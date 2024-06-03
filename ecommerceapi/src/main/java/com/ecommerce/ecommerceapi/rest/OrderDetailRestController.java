package com.ecommerce.ecommerceapi.rest;

import com.ecommerce.ecommerceapi.dto.OrderDetailDTO;
import com.ecommerce.ecommerceapi.entity.Order;
import com.ecommerce.ecommerceapi.entity.OrderDetail;
import com.ecommerce.ecommerceapi.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/orderDetail")
public class OrderDetailRestController {

    private OrderDetailService orderDetailService;

    @Autowired
    public OrderDetailRestController(OrderDetailService theOrderDetailService){
        orderDetailService = theOrderDetailService;
    }


    @GetMapping("")
    public List<OrderDetail> findAll(){
        return orderDetailService.findAll();
    }


    @GetMapping("/{orderDetailId}")
    public ResponseEntity<OrderDetailDTO> getOrderDetail(@PathVariable Long orderDetailId) {
        OrderDetailDTO theOrderDetailDTO = orderDetailService.findById(orderDetailId);
        return ResponseEntity.ok(theOrderDetailDTO);
    }


    @PostMapping("/addOrderDetail")
    public OrderDetail addOrderDetail(@RequestBody OrderDetail theOrderDetail){
        theOrderDetail.setId(0L);
        OrderDetail dbOrder = orderDetailService.save(theOrderDetail);
        return dbOrder;
    }


    @PutMapping("/updateOrderDetail/{orderDetailId}")
    public OrderDetail updateOrderDetail(@PathVariable Long orderDetailId,@RequestBody OrderDetail theOrderDetail){
        OrderDetail dbOrder = orderDetailService.updateOrderDetail(orderDetailId, theOrderDetail);
        return dbOrder;
    }


    @DeleteMapping("/deleteCategory/{orderDetailId}")
    public ResponseEntity<String> deleteOrderDetail(@PathVariable Long orderDetailId) {

        orderDetailService.findById(orderDetailId);

        orderDetailService.deleteById(orderDetailId);
        return ResponseEntity.ok("Deleted order detail id - " + orderDetailId);
    }
}
