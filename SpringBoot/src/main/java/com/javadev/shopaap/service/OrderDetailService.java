package com.javadev.shopaap.service;

import com.javadev.shopaap.dto.OrderDetailDTO;
import com.javadev.shopaap.entity.OrderDetailEntity;
import com.javadev.shopaap.exception.DataNotFoundException;

import java.util.List;

public interface OrderDetailService {
    OrderDetailEntity createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception;
    OrderDetailEntity getOrderDetail(Long id) throws DataNotFoundException;
    OrderDetailEntity updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws DataNotFoundException;
    void deleteById(Long id);
    List<OrderDetailEntity> findByOrderId(Long orderId);
}
