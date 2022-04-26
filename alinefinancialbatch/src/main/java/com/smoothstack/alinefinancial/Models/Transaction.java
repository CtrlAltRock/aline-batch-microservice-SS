package com.smoothstack.alinefinancial.Models;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name="user")
@XmlAccessorType(XmlAccessType.FIELD)
@Slf4j(topic = "Transaction")
public class Transaction {
    private Long user;
    private Long card;
    private Integer year;
    private Integer month;
    private Integer day;
    private String time;
    private String amount;
    private String method;
    private String merchant_name;
    private String merchant_city;
    private String merchant_state;
    private String merchant_zip;
    private String mcc;
    private String errors;
    private String fraud;
}
