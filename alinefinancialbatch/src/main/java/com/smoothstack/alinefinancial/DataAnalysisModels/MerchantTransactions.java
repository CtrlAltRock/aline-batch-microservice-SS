package com.smoothstack.alinefinancial.DataAnalysisModels;

import com.smoothstack.alinefinancial.Models.Merchant;
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
@XmlRootElement(name="MerchantTransactions")
@XmlAccessorType(XmlAccessType.FIELD)
@Slf4j(topic = "MerchantTransactions")
public class MerchantTransactions {

    private Merchant merchant;

    private String recurringTransactions;

}
