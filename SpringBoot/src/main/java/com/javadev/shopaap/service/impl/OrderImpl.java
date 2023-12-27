package com.javadev.shopaap.service.impl;

import com.javadev.shopaap.dto.OrderDTO;
import com.javadev.shopaap.dto.responses.OrderResponse;
import com.javadev.shopaap.entity.OrderEntity;
import com.javadev.shopaap.entity.OrderStatus;
import com.javadev.shopaap.entity.UserEntity;
import com.javadev.shopaap.exception.DataNotFoundException;
import com.javadev.shopaap.repositories.OrderRepository;
import com.javadev.shopaap.repositories.UserRepository;
import com.javadev.shopaap.service.OrderService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderImpl implements OrderService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public OrderEntity createOrder(OrderDTO orderDTO) throws Exception {
        UserEntity userEntity = userRepository.findById(orderDTO
                        .getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cant find UserID" + orderDTO.getUserId()));

        OrderEntity order = modelMapper.map(orderDTO, OrderEntity.class);
        order.setUser(userEntity);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        LocalDate shippingDate = orderDTO.getShippingDate() == null
                ? LocalDate.now() : orderDTO.getShippingDate();
        if (shippingDate.isBefore(LocalDate.now())) {
            throw new DataNotFoundException("Date must be at least today !");
        }
        order.setShippingDate(shippingDate);
        order.setActive(true);
        orderRepository.save(order);

        return order;
    }

    @Override
    public OrderEntity getOrder(Long id) {

        return orderRepository.findById(id).orElseThrow(null);
    }

    @Override
    public OrderEntity updateOrder(Long id, OrderDTO orderDTO) throws Exception {
        OrderEntity order = orderRepository.findById(id).orElseThrow(() ->
                new DataNotFoundException("Cant find Order Id :" + id));
        UserEntity exitingUser = userRepository
                .findById(orderDTO.
                        getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find user with id: " + id));
       OrderEntity orderEntity= modelMapper.map(orderDTO,OrderEntity.class);
       orderEntity.setUser(exitingUser);
        return  orderRepository.save(orderEntity);
    }
    @Override
    public void deleteOrder(Long id) {
        OrderEntity order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            order.setActive(false);
            orderRepository.save(order);
        }
    }

    @Override
    public List<OrderEntity> findByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
