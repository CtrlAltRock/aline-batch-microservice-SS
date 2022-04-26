package com.smoothstack.alinefinancial.Caches;

import com.smoothstack.alinefinancial.Generators.MerchantGenerator;
import com.smoothstack.alinefinancial.Models.Merchant;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Component
public class MerchantCache {

    private final HashMap<String, Merchant> generatedMerchants = new HashMap<>();
    private static MerchantGenerator merchantGenerator = MerchantGenerator.getInstance();
    private final Set<String> seenMerchants = new HashSet<>();
    private static MerchantCache merchantCacheInstance = null;

    public static MerchantCache getInstance() {
        if(merchantCacheInstance == null) merchantCacheInstance = new MerchantCache();
        return merchantCacheInstance;
    }

    public synchronized void addGeneratedMerchant(String nameId, Merchant merchant) {
        generatedMerchants.put(nameId, merchant);
    }

    public Merchant getGeneratedMerchant(String nameId) {
        return generatedMerchants.get(nameId);
    }

    public Set<String> getSeenMerchants(){
        return seenMerchants;
    }

    public void setSeenMerchants(String id){
        seenMerchants.add(id);
    }

    public HashMap<String, Merchant> getGeneratedMerchants() {
        return generatedMerchants;
    }

    public Merchant findMerchantOrGenerate(String nameId, String code) {
        if(getGeneratedMerchant(nameId) == null) {
            synchronized (MerchantGenerator.class) {
                if(getGeneratedMerchant(nameId) == null) {
                    merchantGenerator.generateMerchant(nameId, code,  this);
                }
            }
        }
        return getGeneratedMerchant(nameId);
    }





}
