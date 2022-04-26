package com.smoothstack.alinefinancial.Generators;

import com.github.javafaker.Faker;
import com.smoothstack.alinefinancial.Caches.MerchantCache;
import com.smoothstack.alinefinancial.Models.Card;
import com.smoothstack.alinefinancial.Models.Merchant;

import java.util.HashMap;

public class MerchantGenerator {

    private static MerchantGenerator merchantGeneratorInstance = null;

    private final Faker faker = new Faker();


    public static MerchantGenerator getInstance() {
        if(merchantGeneratorInstance == null) {
            merchantGeneratorInstance = new MerchantGenerator();
        }
        return merchantGeneratorInstance;
    }

    public synchronized Merchant generateMerchant(String name, MerchantCache mc) {
        Merchant merchant = new Merchant();
        merchant.setName(faker.company().name());
        mc.addGeneratedMerchant(name, merchant);
        //System.out.println(merchant);
        return merchant;
    }

}
