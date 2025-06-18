package com.example.pharmacy15.controller;

import com.example.pharmacy15.aop.AlertMessageStore;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertMessageStore alertStore;

    @GetMapping
    public List<String> getAlerts() {
        return alertStore.getAll();
    }
}