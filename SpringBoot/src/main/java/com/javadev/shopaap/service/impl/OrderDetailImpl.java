package com.javadev.shopaap.service.impl;

import com.javadev.shopaap.dto.OrderDetailDTO;
import com.javadev.shopaap.entity.OrderDetailEntity;
import com.javadev.shopaap.entity.OrderEntity;
import com.javadev.shopaap.entity.ProductEntity;
import com.javadev.shopaap.exception.DataNotFoundException;
import com.javadev.shopaap.repositories.OrderDetailRepository;
import com.javadev.shopaap.repositories.OrderRepository;
import com.javadev.shopaap.repositories.ProductRepository;
import com.javadev.shopaap.service.OrderDetailService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailImpl implements OrderDetailService {
    @Autowired
    OrderDetailRepository orderDetailRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ModelMapper modelMapper;
    @Override
    public OrderDetailEntity createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception {
        OrderEntity order=orderRepository
                .findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new  DataNotFoundException("Order Id not found" + orderDetailDTO.getOrderId()));
        ProductEntity productEntity=productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new  DataNotFoundException("Product Id not found" + orderDetailDTO.getProductId()));

//        OrderDetailEntity orderDetailEntity =modelMapper.map(orderDetailDTO,OrderDetailEntity.class);
        //Không dùng model mapper được vì lỗi không biết dùng ID nào
        OrderDetailEntity orderDetailEntity = new OrderDetailEntity();
        orderDetailEntity.setPrice(orderDetailDTO.getPrice());
        orderDetailEntity.setColor(orderDetailDTO.getColor());
        orderDetailEntity.setTotalMoney(orderDetailDTO.getTotalMoney());
        orderDetailEntity.setNumberOfProducts(orderDetailDTO.getNumberOfProducts());
        orderDetailEntity.setOrder(order);
        orderDetailEntity.setProduct(productEntity);
        return orderDetailRepository.save(orderDetailEntity);
    }

    @Override
    public OrderDetailEntity getOrderDetail(Long id) throws DataNotFoundException {
        return orderDetailRepository.findById(id)
                .orElseThrow(()->new DataNotFoundException("Cannot find OrderDetail with id: "+id));
    }

    @Override
    public OrderDetailEntity updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws DataNotFoundException {
        OrderDetailEntity existingOrderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find order detail with id: "+id));
        OrderEntity existingOrder = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find order with id: "+id));
        ProductEntity existingProduct = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot find product with id: " + orderDetailDTO.getProductId()));
        existingOrderDetail.setPrice(orderDetailDTO.getPrice());
        existingOrderDetail.setNumberOfProducts(orderDetailDTO.getNumberOfProducts());
        existingOrderDetail.setTotalMoney(orderDetailDTO.getTotalMoney());
        existingOrderDetail.setColor(orderDetailDTO.getColor());
        existingOrderDetail.setOrder(existingOrder);
        existingOrderDetail.setProduct(existingProduct);
        return orderDetailRepository.save(existingOrderDetail);
    }

    @Override
    public void deleteById(Long id) {
        orderDetailRepository.deleteById(id);
    }

    @Override
    public List<OrderDetailEntity> findByOrderId(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }
}
