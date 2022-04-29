package com.smoothstack.alinefinancial.Models;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Slf4j(topic = "Merchant")
@XmlRootElement(name="merchant")
@XmlAccessorType(XmlAccessType.FIELD)
public class Merchant {

    @Id
    private String id;

    private String name;

    private String city;

    private String state;

    private String zip;

    private String mcc;
}
