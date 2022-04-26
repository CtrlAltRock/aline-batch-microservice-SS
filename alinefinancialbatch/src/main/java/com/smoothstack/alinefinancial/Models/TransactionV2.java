package com.smoothstack.alinefinancial.Models;


import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Slf4j(topic = "TransactionV2")
public class TransactionV2 {

    @Id
    @XmlElement
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @XmlElement
    private User user;

    @ManyToOne
    @XmlElement
    private Merchant merchant;

    @XmlElement
    private Integer year;

    @XmlElement
    private Integer month;

    @XmlElement
    private Integer day;

    @XmlElement
    private String time;

    @XmlElement
    private String amount;

    @XmlElement
    private String method;

    @XmlElement
    private String errors;

    @XmlElement
    private String fraud;


}
