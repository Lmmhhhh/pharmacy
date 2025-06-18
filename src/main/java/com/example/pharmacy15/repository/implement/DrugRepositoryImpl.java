package com.example.pharmacy15.repository.implement;

import com.example.pharmacy15.domain.Drug;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import org.springframework.stereotype.Repository;
import com.example.pharmacy15.repository.DrugRepository;


import java.util.Optional;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class DrugRepositoryImpl implements DrugRepository {

    private final JdbcTemplate jdbc;

    private final RowMapper<Drug> drugRowMapper = (rs, rowNum) -> {
        Drug d = new Drug();
        d.setDrugId(rs.getInt("drug_id"));
        d.setName(rs.getString("name"));
        d.setIngredient(rs.getString("ingredient"));
        d.setCompany(rs.getString("company"));
        d.setPrescribeType(rs.getString("prescribe_type"));
        d.setEfficacy(rs.getString("efficacy"));
        d.setDosage(rs.getString("dosage"));
        d.setCaution(rs.getString("caution"));
        d.setStoring(rs.getString("storing"));
        d.setTotalSales(rs.getInt("total_sales"));
        return d;
    };

    @Override
    public Optional<Drug> findById(int drugId) {
        String sql = "SELECT * FROM drug WHERE drug_id = ?";
        return jdbc.query(sql, drugRowMapper, drugId)
                .stream().findFirst();
    }


    @Override
    public List<Drug> findAll() {
        String sql = "SELECT * FROM drug";
        return jdbc.query(sql, drugRowMapper);
    }

    @Override
    public List<Drug> searchByKeyword(String keyword) {
        String sql = "SELECT * FROM drug WHERE name LIKE ? OR company LIKE ? OR efficacy LIKE ?";
        return jdbc.query(sql,
                new Object[]{"%" + keyword + "%", "%" + keyword + "%", "%" + keyword + "%"},
                new BeanPropertyRowMapper<>(Drug.class));
    }
}
