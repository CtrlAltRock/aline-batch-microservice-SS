package com.smoothstack.alinefinancial.models;

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
}
