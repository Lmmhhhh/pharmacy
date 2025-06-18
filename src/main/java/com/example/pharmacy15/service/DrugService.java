package com.example.pharmacy15.service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.example.pharmacy15.domain.Drug;
import com.example.pharmacy15.dto.response.DrugResponse;
import com.example.pharmacy15.repository.implement.DrugRepositoryImpl;



@Service
@RequiredArgsConstructor
public class DrugService {

    private final JdbcTemplate jdbc;
    private final DrugRepositoryImpl drugRepository;


    public List<DrugResponse> searchDrugs(String keyword) {
        List<Drug> drugs = drugRepository.searchByKeyword(keyword);

        return drugs.stream()
                .map(drug -> new DrugResponse(
                        drug.getDrugId(),
                        drug.getName(),
                        drug.getCompany(),
                        drug.getEfficacy()
                ))
                .toList();
    }

    public List<Map<String, Object>> getExpiringDrugs() {
        return jdbc.queryForList("SELECT * FROM view_expiring_list");
    }


    public List<Map<String, Object>> getLowStockDrugs() {
        return jdbc.queryForList("SELECT * FROM view_low_stock");
    }
}
