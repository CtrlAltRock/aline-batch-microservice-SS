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

    private Long id;

    private String name;

    private String city;

    private String state;

    private String zip;

    private String mcc;

    private HashMap<Double, Integer> transactionsByAmt;

    public void addAmount(Double amt) {
        try {
            if (transactionsByAmt.containsKey(amt)) {
                transactionsByAmt.put(amt, transactionsByAmt.get(amt) + 1);
            } else {
                transactionsByAmt.put(amt, 1);
            }
        } catch (Exception e) {
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("Method: addAmount\tException: ");
            errorMessage.append(e);
            log.error(errorMessage.toString());
        }
    }
}
