package com.smoothstack.alinefinancial.Generators;

import com.github.javafaker.Faker;
import com.smoothstack.alinefinancial.Maps.MerchantMap;
import com.smoothstack.alinefinancial.Models.Merchant;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "MerchantGenerator")
public class MerchantGenerator {

    private static MerchantGenerator merchantGeneratorInstance = null;

    private final Faker faker = new Faker();

    public static MerchantGenerator getInstance() {
        try {
            if (merchantGeneratorInstance == null) {
                synchronized (MerchantGenerator.class) {
                    if (merchantGeneratorInstance == null) {
                        merchantGeneratorInstance = new MerchantGenerator();
                    }
                }
            }
        } catch (Exception e) {
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("Method: getInstance\tException: ");
            errorMessage.append(e);
            log.error(errorMessage.toString());
        }
        return merchantGeneratorInstance;
    }

    public Merchant generateMerchant(String name, String code, MerchantMap mc) {
        Merchant merchant = new Merchant();
        try {
            String companyName = faker.company().name();
            merchant.setId(name);
            merchant.setName(companyName);
            merchant.setMcc(code);
            mc.addGeneratedMerchant(name, merchant);
        } catch (Exception e) {
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("Method: generateMerchant\tException: ");
            errorMessage.append(e);
            log.error(errorMessage.toString());
        }
        return merchant;
    }

}
