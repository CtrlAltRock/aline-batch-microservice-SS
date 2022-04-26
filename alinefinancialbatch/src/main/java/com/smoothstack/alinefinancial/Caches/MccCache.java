package com.smoothstack.alinefinancial.Caches;

import com.smoothstack.alinefinancial.Generators.MccGenerator;
import com.smoothstack.alinefinancial.Models.MCC;

import java.util.HashMap;

public class MccCache {
    private HashMap<String, MCC> generatedMccs = new HashMap<>();
    private final MccGenerator mccGenerator = MccGenerator.getInstance();

    public void addGeneratedMCCs(String MccId, MCC mcc){
        generatedMccs.put(MccId, mcc);
    }

    public MCC getGeneratedMcc(String MccId){
        return generatedMccs.get(MccId);
    }

    public HashMap<String, MCC> getGeneratedMccs() {
        return generatedMccs;
    }

    public MCC findMccOrGenerate(String mccId) {
        if(getGeneratedMcc(mccId) == null) {
            synchronized (MccGenerator.class) {
                if(getGeneratedMcc(mccId) == null) {
                    mccGenerator.generateMcc(mccId, this);
                }
            }
        }
        return getGeneratedMcc(mccId);
    }
}
