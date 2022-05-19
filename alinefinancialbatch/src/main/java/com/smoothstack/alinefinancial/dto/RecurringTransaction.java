package com.smoothstack.alinefinancial.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name="RecurringTransaction")
@XmlAccessorType(XmlAccessType.FIELD)
@Slf4j(topic = "RecurringTransaction")
public class RecurringTransaction {
    private String merchantId;
    private BigDecimal amount;
    private Long userId;
    private Long cardId;
}
