package com.smoothstack.alinefinancial.dto;

import com.smoothstack.alinefinancial.models.Merchant;
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
@XmlRootElement(name="MerchantBalanceErrors")
@XmlAccessorType(XmlAccessType.FIELD)
@Slf4j(topic = "MerchantBalanceErrors")
public class MerchantBalanceErrors {

    private Merchant merchant;
    private Long insufficientBalanceErrors;
}
