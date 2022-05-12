package com.smoothstack.alinefinancial.DataAnalysisModels;

import com.smoothstack.alinefinancial.Models.Transaction;
import com.smoothstack.alinefinancial.Models.User;
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
@XmlRootElement(name="UserDeposit")
@XmlAccessorType(XmlAccessType.FIELD)
@Slf4j(topic = "UserDeposit")
public class UserDeposit {

    private User user;

    private List<Transaction> deposits;


    public synchronized void addDeposit(Transaction item) {
        try {
            deposits.add(item);
        } catch (Exception e) {
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("Method: addDeposit\tException: ");
            errorMessage.append(e);
            log.error(errorMessage.toString());
        }
    }



}
