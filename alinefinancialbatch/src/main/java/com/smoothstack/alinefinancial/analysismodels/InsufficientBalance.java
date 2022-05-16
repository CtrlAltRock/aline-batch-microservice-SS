package com.smoothstack.alinefinancial.analysismodels;


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
@XmlRootElement(name="InsufficientBalance")
@XmlAccessorType(XmlAccessType.FIELD)
@Slf4j(topic = "InsufficientBalance")
public class InsufficientBalance {

    private Long numberOfUsers;

    private Long atLeastOnce;

    private Long moreThanOnce;

    private Double percentageAtLeastOnce;

    private Double percentageMoreThanOnce;

}
