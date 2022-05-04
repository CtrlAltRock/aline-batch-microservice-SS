package com.smoothstack.alinefinancial.Maps;

import com.smoothstack.alinefinancial.Generators.MerchantGenerator;
import com.smoothstack.alinefinancial.Models.Merchant;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j(topic="MerchantCache")
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

    public void addGeneratedMerchant(String nameId, Merchant merchant) {
        generatedMerchants.put(nameId, merchant);
    }

    public Merchant getGeneratedMerchant(String nameId) {
        return generatedMerchants.get(nameId);
    }

    public HashMap<String, Merchant> getGeneratedMerchants() {
        return generatedMerchants;
    }

    public Merchant findMerchantOrGenerate(String nameId, String code, String amt) {
        if(getGeneratedMerchant(nameId) == null) {
            synchronized (MerchantMap.class) {
                if(getGeneratedMerchant(nameId) == null) {
                    Merchant merchant = merchantGenerator.generateMerchant(nameId, code, amt,  this);
                }
            }
        }
        return getGeneratedMerchant(nameId);
    }





}
