package com.smoothstack.alinefinancial.Models;


import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalTime;

@Getter
@Setter
@ToString
@XmlRootElement
@NoArgsConstructor
@AllArgsConstructor
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

    @XmlElement
    private String merchant_zip;

    @XmlElement
    private String mcc;

    @XmlElement
    private String errors;

    @XmlElement
    private String fraud;
}
