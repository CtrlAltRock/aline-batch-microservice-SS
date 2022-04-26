package com.smoothstack.alinefinancial.Caches;

import com.smoothstack.alinefinancial.Generators.MerchantGenerator;
import com.smoothstack.alinefinancial.Models.Merchant;

import java.util.HashMap;

public class MerchantCache {

    private final HashMap<String, Merchant> generatedMerchants = new HashMap<>();
    private static MerchantGenerator merchantGenerator = MerchantGenerator.getInstance();

    public synchronized void addGeneratedMerchant(String nameId, Merchant merchant) {
        generatedMerchants.put(nameId, merchant);
    }

    public Merchant getGeneratedMerchant(String nameId) {
        return generatedMerchants.get(nameId);
    }

    public HashMap<String, Merchant> getGeneratedMerchants() {
        return generatedMerchants;
    }

    public Merchant findMerchantOrGenerate(String nameId) {
        if(getGeneratedMerchant(nameId) == null) {
            synchronized (MerchantGenerator.class) {
                if(getGeneratedMerchant(nameId) == null) {
                    merchantGenerator.generateMerchant(nameId, this);
                }
            }
        }
        return getGeneratedMerchant(nameId);
    }





}
