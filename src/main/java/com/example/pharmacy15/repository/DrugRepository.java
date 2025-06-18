package com.example.pharmacy15.repository;

import com.example.pharmacy15.domain.Drug;
import java.util.List;

import java.util.Optional;

public interface DrugRepository {
    Optional<Drug> findById(int drugId);

    List<Drug> findAll(); //test용

    List<Drug> searchByKeyword(String keyword);
}
