package com.javadev.shopaap.controller;

import com.javadev.shopaap.dto.OrderDetailDTO;
import com.javadev.shopaap.exception.DataNotFoundException;
import com.javadev.shopaap.service.OrderDetailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/order_details")
public class OrderDetailsController {
    @Autowired
    OrderDetailService orderDetailService;
    //Thêm mới 1 order detail
    @PostMapping("")
    public ResponseEntity<?> createOrderDetail(
            @Valid  @RequestBody OrderDetailDTO newOrderDetail) {
        try {
            return ResponseEntity.ok(orderDetailService.createOrderDetail(newOrderDetail));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(
            @Valid @PathVariable("id") Long id) throws DataNotFoundException {
        return ResponseEntity.ok( orderDetailService.getOrderDetail(id));
    }
    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetails(@Valid @PathVariable("orderId") Long orderId) {

        return ResponseEntity.ok(orderDetailService.findByOrderId(orderId));
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(
            @Valid @PathVariable("id") Long id,
            @RequestBody OrderDetailDTO newOrderDetailData) throws DataNotFoundException {

        return ResponseEntity.ok(orderDetailService.updateOrderDetail(id,newOrderDetailData));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrderDetail(
            @Valid @PathVariable("id") Long id) {
        orderDetailService.deleteById(id);
        return ResponseEntity.ok()
                .body("Delete successfully");
    }
}
