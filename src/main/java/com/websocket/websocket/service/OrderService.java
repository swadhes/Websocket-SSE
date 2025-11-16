package com.websocket.websocket.service;

import com.websocket.websocket.entity.Order;
import com.websocket.websocket.repo.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private SseService sseService;

    public Order placeOrder(Order order) {

        Order saved = orderRepository.save(order);

        // Notify ONLY this user
        String message = "🛒 Order placed: " + saved.getProductName()
                + " | Amount: ₹" + saved.getAmount();

        sseService.sendToUser(saved.getUserId(), message);

        return saved;
    }
}
