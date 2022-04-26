package com.smoothstack.alinefinancial.Models;

import com.github.javafaker.Faker;
import com.sun.istack.NotNull;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@ToString
@XmlRootElement
@NoArgsConstructor
@AllArgsConstructor
@Slf4j(topic = "Merchant")
public class Merchant {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @NotNull
    private String name;

    @NotNull
    private String city;

    private String state;

    private String zip;

    @ManyToOne
    private MCC mcc;
}
