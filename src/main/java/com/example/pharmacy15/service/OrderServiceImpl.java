package com.example.pharmacy15.service;

import com.example.pharmacy15.dto.request.OtcOrderRequest;
import com.example.pharmacy15.dto.request.PrescriptionOrderRequest;
import com.example.pharmacy15.dto.request.MultiOrderRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.Types;
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
            jdbc.queryForMap(
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
                    new SqlParameterValue(Types.VARCHAR, itemsJson)  // JSON 명시적 전달
            );

            String msg;
            if (request.isPrescription()) {
                msg = "처방약 주문이 완료되었습니다.";
            } else {
                String memberInfo;
                try {
                    Integer patientId = jdbc.queryForObject(
                            "SELECT patient_id FROM patient WHERE patient_name = ? AND RIGHT(phone, 4) = ?",
                            Integer.class,
                            request.getPatientName(),
                            request.getPhone()
                    );
                    memberInfo = "(회원 ID: " + patientId + ")";
                } catch (Exception ignored) {
                    memberInfo = "(비회원)";
                }
                msg = "일반약 주문이 완료되었습니다. " + memberInfo;
            }
            return Map.of("message", msg);

        } catch (Exception e) {
            String raw = Optional.ofNullable(e.getCause()).map(Throwable::getMessage).orElse(e.getMessage());
            String msg;

            // SIGNAL 메시지 추출
            if (raw.contains("SQL") && raw.contains("MESSAGE_TEXT")) {
                int idx = raw.lastIndexOf("MESSAGE_TEXT");
                msg = raw.substring(idx).replace("MESSAGE_TEXT", "").replaceAll("[:;=]", "").trim();
            }
            // SQL 문법 오류 처리
            else if (raw.contains("bad SQL grammar")) {
                msg = "서버 내 SQL 호출 형식 오류 (문법 오류)";
            }
            // 기본 처리
            else {
                msg = raw.contains(":") ? raw.substring(raw.lastIndexOf(":") + 1).trim() : raw;
            }

            return Map.of("status", 400, "message", "주문 실패: " + msg);
        }
    }

    @Override
    public Map<String, Object> handleOtcOrder(OtcOrderRequest request) {
        MultiOrderRequest multi = new MultiOrderRequest();
        multi.setPrescription(false);

        for (OtcOrderRequest.DrugItem item : request.getItems()) {
            Integer drugId = jdbc.queryForObject(
                    "SELECT drug_id FROM drug WHERE name = ?",
                    Integer.class,
                    item.getDrugName()
            );
            String prescribeType = jdbc.queryForObject(
                    "SELECT prescribe_type FROM drug WHERE drug_id = ?",
                    String.class,
                    drugId
            );
            if ("처방".equals(prescribeType)) {
                throw new IllegalArgumentException("처방약은 일반 구매할 수 없습니다: " + item.getDrugName());
            }
        }

        try {
            jdbc.queryForObject(
                    "SELECT patient_id FROM patient WHERE patient_name = ? AND RIGHT(phone, 4) = ?",
                    Integer.class,
                    request.getName(),
                    request.getPhoneLast4()
            );
        } catch (Exception ignored) {}

        multi.setPatientName(request.getName());
        multi.setPhone(request.getPhoneLast4());
        multi.setSaleDate(request.getSaleDate());

        List<MultiOrderRequest.DrugItem> items = request.getItems().stream()
                .map(otc -> {
                    MultiOrderRequest.DrugItem item = new MultiOrderRequest.DrugItem();
                    item.setDrugName(otc.getDrugName());
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
            String name = item.getDrugName().trim();

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