package com.example.pharmacy15.controller;

import com.example.pharmacy15.dto.request.OtcOrderRequest;
import com.example.pharmacy15.dto.request.PrescriptionOrderRequest;
import com.example.pharmacy15.dto.response.MultiOrderResponse;
import com.example.pharmacy15.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/otc")
    public ResponseEntity<?> processOtc(@RequestBody OtcOrderRequest request) {
        try {
            Map<String, Object> result = orderService.handleOtcOrder(request);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", e.getMessage(),
                    "status", 400
            ));
        }
    }

    @PostMapping("/prescription")
    public ResponseEntity<?> processPrescription(@RequestBody PrescriptionOrderRequest request) {
        try {
            // processPrescriptionOrder → handlePrescriptionOrder
            Map<String, Object> result = orderService.handlePrescriptionOrder(request);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", e.getMessage(),
                    "status", 400
            ));
        }
    }
}
