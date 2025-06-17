package com.example.pharmacy15.service;

import com.example.pharmacy15.dto.request.OtcOrderRequest;
import com.example.pharmacy15.dto.request.PrescriptionOrderRequest;
import com.example.pharmacy15.dto.request.MultiOrderRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.EmptyResultDataAccessException;


import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class OrderServiceImpl implements OrderService {

    private final JdbcTemplate jdbc;

    @Override
    public Map<String, Object> registerMultiOrder(MultiOrderRequest request) {
        String itemsJson = toJsonArray(request.getItems(), request.isPrescription());
        try {
            return jdbc.queryForMap(
                    "CALL proc_register_multi_order(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    request.isPrescription(),
                    request.getPatientName(),
                    request.getBirthDate(),
                    request.getGender(),
                    request.getPhone(),
                    request.getAddress(),
                    request.getAllergy(),
                    request.getDoctor(),
                    request.getHospital(),
                    request.getIssuedDate(),
                    request.getSaleDate(),
                    itemsJson
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("주문 실패: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> handleOtcOrder(OtcOrderRequest request) {
        MultiOrderRequest multi = new MultiOrderRequest();
        multi.setPrescription(false);

        // 회원 여부만 확인 (insert X)
        try {
            jdbc.queryForObject(
                    "SELECT patient_id FROM patient WHERE patient_name = ? AND RIGHT(phone, 4) = ?",
                    Integer.class,
                    request.getName(),
                    request.getPhoneLast4()
            );
        } catch (Exception ignored) {
            // 회원이 아니면 patient_id = null 그대로 사용
        }

        multi.setPatientName(request.getName());
        multi.setPhone(request.getPhoneLast4());
        multi.setSaleDate(request.getSaleDate());

        // ✅ 직접 입력된 drugName을 그대로 사용
        List<MultiOrderRequest.DrugItem> items = request.getItems().stream()
                .map(otc -> {
                    MultiOrderRequest.DrugItem item = new MultiOrderRequest.DrugItem();
                    item.setDrugName(otc.getDrugName());  // 더 이상 DB 조회 안 함
                    item.setQuantity(otc.getQuantity());
                    return item;
                })
                .collect(Collectors.toList());

        multi.setItems(items);
        return registerMultiOrder(multi);
    }

    @Override
    public Map<String, Object> handlePrescriptionOrder(PrescriptionOrderRequest request) {
        MultiOrderRequest multi = new MultiOrderRequest();
        multi.setPrescription(true);
        multi.setPatientName(request.getPatientName());
        multi.setBirthDate(request.getBirthDate());
        multi.setGender(request.getGender());
        multi.setPhone(request.getPhone());
        multi.setAddress(request.getAddress());
        multi.setAllergy(request.getAllergy());
        multi.setDoctor(request.getDoctor());
        multi.setHospital(request.getHospital());
        multi.setIssuedDate(request.getIssuedDate());
        multi.setSaleDate(request.getSaleDate());

        List<MultiOrderRequest.DrugItem> items = request.getItems().stream()
                .map(rx -> {
                    MultiOrderRequest.DrugItem item = new MultiOrderRequest.DrugItem();
                    item.setDrugName(rx.getDrugName());
                    item.setQuantity(rx.getQuantity());
                    item.setDosage(rx.getDosage());
                    item.setMedicGuide(rx.getMedicGuide());
                    return item;
                })
                .collect(Collectors.toList());

        multi.setItems(items);
        return registerMultiOrder(multi);
    }

    private String toJsonArray(List<MultiOrderRequest.DrugItem> items, boolean isPrescription) {
        List<Map<String, Object>> converted = new ArrayList<>();

        for (MultiOrderRequest.DrugItem item : items) {
            String name = item.getDrugName().trim();  // ✅ 공백 제거
            System.out.println("🔍 drugName 요청 값: [" + name + "]");  // ✅ 콘솔 디버깅

            Integer drugId;
            try {
                drugId = jdbc.queryForObject(
                        "SELECT drug_id FROM drug WHERE name = ?",
                        Integer.class,
                        name
                );
            } catch (EmptyResultDataAccessException e) {
                throw new IllegalArgumentException("존재하지 않는 약품명: " + name);
            }

            Map<String, Object> m = new HashMap<>();
            m.put("drugId", drugId);
            m.put("quantity", item.getQuantity());

            if (isPrescription) {
                m.put("dosage", item.getDosage());
                m.put("medicGuide", item.getMedicGuide());
            }
            converted.add(m);
        }

        try {
            return new ObjectMapper().writeValueAsString(converted);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 변환 실패", e);
        }
    }
}