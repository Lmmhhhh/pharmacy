package com.example.pharmacy15.repository.implement;

import com.example.pharmacy15.domain.Order;
import com.example.pharmacy15.repository.iface.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final JdbcTemplate jdbc;

    @Override
    public int save(Order order) {
        String sql = "INSERT INTO orders (patient_id, sale_date, total_price) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            if (order.getPatientId() != null) {
                ps.setInt(1, order.getPatientId());
            } else {
                ps.setNull(1, java.sql.Types.INTEGER);
            }
            ps.setDate(2, java.sql.Date.valueOf(order.getSaleDate()));
            ps.setInt(3, order.getTotalPrice());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue(); // 생성된 order_id 반환
    }

    @Override
    public void updateTotalPrice(int orderId, int totalPrice) {
        String sql = "UPDATE orders SET total_price = ? WHERE order_id = ?";
        jdbc.update(sql, totalPrice, orderId);
    }
}
