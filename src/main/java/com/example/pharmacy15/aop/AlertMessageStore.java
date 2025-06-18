
package com.example.pharmacy15.aop;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class AlertMessageStore {

    private final LinkedList<String> messages = new LinkedList<>();

    public void add(String message) {
        if (messages.size() > 10) {
            messages.removeFirst(); // 최대 10개만 유지
        }
        messages.add(message);
    }

    public List<String> getAll() {
        return new LinkedList<>(messages);
    }
}