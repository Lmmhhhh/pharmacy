package com.example.pharmacy15.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stats")
public class StatController {

    private final JdbcTemplate jdbc;

    // 기간별 약품 판매 통계
    @GetMapping("/sales")
    public ResponseEntity<List<Map<String, Object>>> salesStat(
            @RequestParam String start,
            @RequestParam String end) {
        String sql = "CALL proc_sales_stat(?, ?)";
        return ResponseEntity.ok(jdbc.queryForList(sql, start, end));
    }

    // 주간 통계
    @GetMapping("/weekly")
    public ResponseEntity<List<Map<String, Object>>> getWeekly(
            @RequestParam int year,
            @RequestParam int month) {
        String sql = "CALL proc_sales_weekly(?, ?)";
        return ResponseEntity.ok(jdbc.queryForList(sql, year, month));
    }

    // 월간 통계
    @GetMapping("/monthly")
    public ResponseEntity<List<Map<String, Object>>> getMonthly(
            @RequestParam int year,
            @RequestParam int month) {
        String sql = "CALL proc_sales_monthly(?, ?)";
        return ResponseEntity.ok(jdbc.queryForList(sql, year, month));
    }

    // 약품별 판매 top3
    @GetMapping("/top3")
    public ResponseEntity<List<Map<String, Object>>> getTop3(
            @RequestParam int year,
            @RequestParam int month) {
        String sql = "CALL proc_sales_top3(?, ?)";
        return ResponseEntity.ok(jdbc.queryForList(sql, year, month));
    }
}
