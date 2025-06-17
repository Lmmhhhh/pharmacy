package com.example.pharmacy15.repository;

import com.example.pharmacy15.domain.Order;

public interface OrderRepository {
    int save(Order order); // 주문 저장 → 생성된 order_id 반환
    void updateTotalPrice(int orderId, int totalPrice); // 총 판매 금액 업데이트
}

