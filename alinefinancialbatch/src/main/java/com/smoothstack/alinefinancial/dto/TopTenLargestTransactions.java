package com.smoothstack.alinefinancial.dto;

import com.smoothstack.alinefinancial.models.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name="TopTenLargestTransactions")
@XmlAccessorType(XmlAccessType.FIELD)
@Slf4j(topic = "TopTenLargestTransactions")
public class TopTenLargestTransactions {

    private List<Transaction> transactions;

}
