package com.example.pharmacy15.repository.implement;

import com.example.pharmacy15.domain.Purchase;
import com.example.pharmacy15.repository.iface.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PurchaseRepositoryImpl implements PurchaseRepository {

    private final JdbcTemplate jdbc;

    private final RowMapper<Purchase> purchaseRowMapper = (rs, rowNum) -> {
        Purchase p = new Purchase();
        p.setPurchaseId(rs.getInt("purchase_id"));
        p.setDrugId(rs.getInt("drug_id"));
        p.setQuantity(rs.getInt("quantity"));
        p.setRemainingQuantity(rs.getInt("remaining_quantity"));
        p.setUnitPrice(rs.getBigDecimal("unit_price"));
        p.setPurchaseDate(rs.getDate("purchase_date").toLocalDate());
        p.setExpirationDate(rs.getDate("expiration_date").toLocalDate());
        p.setSupplier(rs.getString("supplier"));
        return p;
    };

    @Override
    public List<Purchase> findAvailableByDrugId(int drugId) {
        String sql = "SELECT * FROM purchase WHERE drug_id = ? AND remaining_quantity > 0 ORDER BY purchase_date ASC";
        return jdbc.query(sql, purchaseRowMapper, drugId);
    }

    @Override
    public void updateRemainingQuantity(int purchaseId, int remaining) {
        String sql = "UPDATE purchase SET remaining_quantity = ? WHERE purchase_id = ?";
        jdbc.update(sql, remaining, purchaseId);
    }
}
