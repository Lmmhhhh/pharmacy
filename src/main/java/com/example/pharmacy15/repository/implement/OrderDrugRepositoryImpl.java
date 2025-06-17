package com.example.pharmacy15.repository.implement;

import com.example.pharmacy15.repository.OrderDrugRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderDrugRepositoryImpl implements OrderDrugRepository {

    private final JdbcTemplate jdbc;

    @Override
    public void save(int orderId, int drugId, int saleQuantity, int salePrice) {
        String sql = "INSERT INTO order_drug (order_id, drug_id, sale_quantity, sale_price) VALUES (?, ?, ?, ?)";
        jdbc.update(sql, orderId, drugId, saleQuantity, salePrice);
    }
}

