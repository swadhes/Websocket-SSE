package com.websocket.websocket.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class SseService {

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final Map<Long, SseEmitter> userEmitters = new ConcurrentHashMap<>();


    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));

        return emitter;
    }

    public void sendEvent(String event) {
        List<SseEmitter> deadEmitters = new ArrayList<>();

        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event().name("notification").data(event));
            } catch (Exception e) {
                deadEmitters.add(emitter);
            }
        });

        emitters.removeAll(deadEmitters);
    }
    public SseEmitter subscribe(Long userId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        userEmitters.put(userId, emitter);

        emitter.onCompletion(() -> userEmitters.remove(userId));
        emitter.onTimeout(() -> userEmitters.remove(userId));

        return emitter;
    }

    // Send to a specific user
    public void sendToUser(Long userId, String message) {
        SseEmitter emitter = userEmitters.get(userId);

        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("notification").data(message));
            } catch (Exception e) {
                userEmitters.remove(userId);
            }
        }
    }

    // Send to all users (optional)
    public void sendToAll(String message) {
        userEmitters.forEach((id, emitter) -> {
            try {
                emitter.send(SseEmitter.event().name("notification").data(message));
            } catch (Exception e) {
                userEmitters.remove(id);
            }
        });
    }
}

