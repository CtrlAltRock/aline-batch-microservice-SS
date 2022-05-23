package com.smoothstack.alinefinancial.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name="CityAndTransactionVolume")
@XmlAccessorType(XmlAccessType.FIELD)
@Slf4j(topic = "CityAndTransactionVolume")
public class CityAndTransactionVolume {

    private String city = "city";

    private Long transactionCount = 0L;

}
