package com.smoothstack.alinefinancial.Models;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Slf4j(topic = "Merchant")
@XmlRootElement(name="merchant")
@XmlAccessorType(XmlAccessType.FIELD)
public class Merchant {

    private String id;

    private String name;

    private String city;

    private String state;

    private String zip;

    private String mcc;

    private HashMap<String, Integer> transactionsByAmt;

    public void addAmount(String amt) {
        if(transactionsByAmt.containsKey(amt)) {
            transactionsByAmt.put(amt, transactionsByAmt.get(amt)+1);
        }
        else {
            transactionsByAmt.put(amt, 1);
        }
    }
}
