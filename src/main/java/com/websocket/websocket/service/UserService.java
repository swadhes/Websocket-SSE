package com.websocket.websocket.service;

import com.websocket.websocket.entity.User;
import com.websocket.websocket.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SseService sseService;

    public User createUser(User user) {
        User saved = userRepository.save(user);

        // Send SSE Notification
        String message = "New user onboarded: " + saved.getName();
        sseService.sendEvent(message);

        return saved;
    }
}


