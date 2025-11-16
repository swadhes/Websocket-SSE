package com.websocket.websocket.controller;

import com.websocket.websocket.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sse")
public class SseController {

    @Autowired
    private SseService sseService;

    @GetMapping("/subscribe")
    public SseEmitter subscribe() {
        return sseService.subscribe();
    }

    @GetMapping("/subscribe/{userId}")
    public SseEmitter subscribe(@PathVariable Long userId) {
        return sseService.subscribe(userId);
    }
}

