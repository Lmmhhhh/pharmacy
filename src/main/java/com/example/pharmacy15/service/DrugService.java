package com.example.pharmacy15.service;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pharmacy15.domain.Drug;
import com.example.pharmacy15.dto.response.DrugResponse;
import com.example.pharmacy15.repository.implement.DrugRepositoryImpl;

@Service
@RequiredArgsConstructor
public class DrugService {

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
}
