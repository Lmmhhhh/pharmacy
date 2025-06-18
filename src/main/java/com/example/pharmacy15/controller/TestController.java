package com.example.pharmacy15.controller;

import com.example.pharmacy15.domain.Drug;
import com.example.pharmacy15.repository.iface.DrugRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

    private final DrugRepository drugRepository;

    @GetMapping("/drugs")
    public List<Drug> getDrugs() {
        return drugRepository.findAll();
    }
}

