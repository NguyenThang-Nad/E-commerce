package com.javadev.shopaap.service;

import com.javadev.shopaap.dto.OrderDTO;
import com.javadev.shopaap.dto.responses.OrderResponse;
import com.javadev.shopaap.entity.OrderEntity;

import java.util.List;

public interface OrderService {
    OrderEntity createOrder(OrderDTO orderDTO) throws Exception;
    OrderEntity getOrder(Long id);
    OrderEntity updateOrder(Long id,OrderDTO orderDTO) throws Exception;
    void deleteOrder(Long id);
    List<OrderEntity> findByUserId(Long userId);
}
