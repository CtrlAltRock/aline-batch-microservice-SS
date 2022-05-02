package com.smoothstack.alinefinancial.Generators;

import com.github.javafaker.Faker;
import com.smoothstack.alinefinancial.Caches.MerchantCache;
import com.smoothstack.alinefinancial.Models.Merchant;

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

    public Merchant generateMerchant(String name, String code, MerchantCache mc) {
        String companyName = faker.company().name();
        Merchant merchant = new Merchant();
        merchant.setName(companyName);
        merchant.setMcc(code);
        mc.addGeneratedMerchant(name, merchant);
        return merchant;
    }

}
