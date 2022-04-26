package com.smoothstack.alinefinancial.Generators;

import com.github.javafaker.Faker;
import com.smoothstack.alinefinancial.Caches.MccCache;
import com.smoothstack.alinefinancial.Models.MCC;

public class MccGenerator {

    private static MccGenerator mccGeneratorInstance = null;

    private final Faker faker = new Faker();

    public static MccGenerator getInstance() {
        if(mccGeneratorInstance == null) {
            mccGeneratorInstance = new MccGenerator();
        }
        return mccGeneratorInstance;
    }

    public synchronized MCC generateMcc(String code, MccCache mc) {
        MCC mcc = new MCC();
        mcc.setCode(code);
        mcc.setType(faker.company().industry());
        mc.addGeneratedMCCs(code, mcc);
        //System.out.println(mcc);
        return mcc;
    }
}
