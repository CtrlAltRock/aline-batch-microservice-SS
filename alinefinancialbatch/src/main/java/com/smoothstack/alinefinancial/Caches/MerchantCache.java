package com.smoothstack.alinefinancial.Caches;

import com.smoothstack.alinefinancial.Generators.MerchantGenerator;
import com.smoothstack.alinefinancial.Models.Merchant;
import com.smoothstack.alinefinancial.Processors.MerchantProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j(topic="MerchantCache")
public class MerchantCache {

    private final HashMap<String, Merchant> generatedMerchants = new HashMap<>();
    private static MerchantGenerator merchantGenerator = MerchantGenerator.getInstance();
    private static MerchantCache merchantCacheInstance = null;

    public static MerchantCache getInstance() {
        if(merchantCacheInstance == null) {
            synchronized (MerchantCache.class) {
                if(merchantCacheInstance == null) {
                    merchantCacheInstance = new MerchantCache();
                }
            }
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

    public Merchant findMerchantOrGenerate(String nameId, String code) {
        if(getGeneratedMerchant(nameId) == null) {
            synchronized (MerchantGenerator.class) {
                if(getGeneratedMerchant(nameId) == null) {
                    Merchant merchant = merchantGenerator.generateMerchant(nameId, code,  this);
                }
            }
        }
        return getGeneratedMerchant(nameId);
    }





}
