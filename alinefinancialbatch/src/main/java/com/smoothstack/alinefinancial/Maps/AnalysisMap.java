package com.smoothstack.alinefinancial.Maps;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

@Slf4j(topic = "AnalysisMap")
public class AnalysisMap {
    private HashMap<String, Object> reportMap = new HashMap<>();
    private static AnalysisMap analysisMapInstance = null;

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
        try {
            reportMap.put(statName, stat);
        } catch (Exception e) {
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("Method: setStatistic\tException: ");
            errorMessage.append(e);
            log.error(errorMessage.toString());
        }
    }
}
