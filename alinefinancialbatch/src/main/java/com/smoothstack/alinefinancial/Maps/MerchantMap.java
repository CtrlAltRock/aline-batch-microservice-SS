package com.smoothstack.alinefinancial.Maps;

import com.smoothstack.alinefinancial.Generators.MerchantGenerator;
import com.smoothstack.alinefinancial.Models.Merchant;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j(topic="MerchantMap")
public class MerchantMap {

    private final HashMap<String, Merchant> generatedMerchants = new HashMap<>();
    private static MerchantGenerator merchantGenerator = MerchantGenerator.getInstance();
    private static MerchantMap merchantCacheInstance = null;

    public static MerchantMap getInstance() {
        if(merchantCacheInstance == null) {
            merchantCacheInstance = new MerchantMap();
        }
        return merchantCacheInstance;
    }

    public synchronized void addGeneratedMerchant(String nameId, Merchant merchant) {
        try {
            generatedMerchants.put(nameId, merchant);
        } catch (Exception e) {
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("Method: addGeneratedMerchant\tException: ");
            errorMessage.append(e);
            log.error(errorMessage.toString());
        }
    }

    public synchronized Merchant getGeneratedMerchant(String nameId) {
        return generatedMerchants.get(nameId);
    }

    public synchronized HashMap<String, Merchant> getGeneratedMerchants() {
        return generatedMerchants;
    }

    public Merchant findMerchantOrGenerate(String nameId, String code) {
        try {
            if (getGeneratedMerchant(nameId) == null) {
                synchronized (MerchantMap.class) {
                    if (getGeneratedMerchant(nameId) == null) {
                        Merchant merchant = merchantGenerator.generateMerchant(nameId, code, this);
                    }
                }
            }
        } catch (Exception e) {
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("Method: findMerchantOrGenerate\tException: ");
            errorMessage.append(e);
            log.error(errorMessage.toString());
        }
        return getGeneratedMerchant(nameId);
    }







}
