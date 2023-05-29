package com.example.springsecurityapplication.services;

import com.example.springsecurityapplication.enumm.Status;
import com.example.springsecurityapplication.models.Order;
import com.example.springsecurityapplication.models.Person;
import com.example.springsecurityapplication.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

@Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order findByID(int id ){
        Optional<Order> order = orderRepository.findById(id);
        return order.orElse(null);
    }

    @Transactional
    public void editStatus(int id, Status status){
        Order order=orderRepository.getReferenceById(id);
        order.setStatus(status);
        orderRepository.save(order);
    }
}
