package com.smoothstack.alinefinancial.Generators;

import com.github.javafaker.Faker;
import com.smoothstack.alinefinancial.Maps.MerchantMap;
import com.smoothstack.alinefinancial.Models.Merchant;

import java.util.HashMap;

public class MerchantGenerator {

    private static MerchantGenerator merchantGeneratorInstance = null;

    private final Faker faker = new Faker();

    public static MerchantGenerator getInstance() {
        if(merchantGeneratorInstance == null) {
            synchronized (MerchantGenerator.class) {
                if(merchantGeneratorInstance == null) {
                    merchantGeneratorInstance = new MerchantGenerator();
                }
            }
        }
        return merchantGeneratorInstance;
    }

    public Merchant generateMerchant(String name, String code, String amt, MerchantMap mc) {
        String companyName = faker.company().name();
        Merchant merchant = new Merchant();
        merchant.setName(companyName);
        merchant.setMcc(code);
        merchant.setTransactionsByAmt(new HashMap<>());
        merchant.addAmount(amt);
        mc.addGeneratedMerchant(name, merchant);
        return merchant;
    }

}
