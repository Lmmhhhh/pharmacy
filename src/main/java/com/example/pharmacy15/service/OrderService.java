package com.example.pharmacy15.service;

import com.example.pharmacy15.dto.request.OtcOrderRequest;
import com.example.pharmacy15.dto.request.PrescriptionOrderRequest;
import com.example.pharmacy15.dto.request.MultiOrderRequest;

import java.util.Map;

public interface OrderService {
    Map<String, Object> registerMultiOrder(MultiOrderRequest request);
    Map<String, Object> handleOtcOrder(OtcOrderRequest request);
    Map<String, Object> handlePrescriptionOrder(PrescriptionOrderRequest request);
}