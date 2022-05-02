package com.smoothstack.alinefinancial.Caches;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import static java.util.Map.entry;

@Component
public class AnalysisMap {
    private HashMap<String, Object> reportMap = new HashMap<>();
    private static AnalysisMap analysisMapInstance = null;

    // Not sure I want to do this just yet or have it added as a statistic from the setStatistic method
    /*public AnalysisMap() {
        reportMap = Map.ofEntries(
                entry("total-unique-merchants", 0),
                entry("number-of-deposits", 0),
                entry("percent-users-at-least-one-insufficient-balance", 0),
                entry("percent-users-more-than-one-insufficient-balance", 0),
                entry("top-5-recurring-transactions-by-merchant",new ArrayList<String>()),
                entry("fraud-by-year", new HashMap<String, ArrayList<Integer>>())
        );
    }*/

    public static AnalysisMap getInstance() {
        if(analysisMapInstance == null) {
            analysisMapInstance = new AnalysisMap();
        }
        return analysisMapInstance;
    }

    public HashMap<String, Object> getReportMap() {
        return reportMap;
    }

    public void setStatistic(String statName, Object stat) {
        reportMap.put(statName, stat);
    }
}
