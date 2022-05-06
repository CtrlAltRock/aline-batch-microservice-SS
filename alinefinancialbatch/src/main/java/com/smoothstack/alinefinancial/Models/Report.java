package com.smoothstack.alinefinancial.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    private Long id;

    private String time;

    private HashMap<String, Object> report;
}
