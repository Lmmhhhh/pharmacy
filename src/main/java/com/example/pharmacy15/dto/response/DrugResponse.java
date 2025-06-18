package com.example.pharmacy15.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter @Setter
@AllArgsConstructor
public class DrugResponse {
    private int drugId;
    private String name;
    private String company;
    private String efficacy;
}
