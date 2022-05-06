package com.smoothstack.alinefinancial.Maps;

import java.util.HashMap;

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
        reportMap.put(statName, stat);
    }
}
