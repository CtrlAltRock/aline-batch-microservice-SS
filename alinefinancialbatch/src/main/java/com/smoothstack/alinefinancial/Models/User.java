package com.smoothstack.alinefinancial.Models;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Slf4j(topic = "User")
@XmlRootElement(name="User")
@XmlAccessorType(XmlAccessType.FIELD)
public class User {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private List<Card> cards;

    private Long insufficientBalanceTransactions;

    private List<Transaction> deposit;

    public synchronized void setCard(Card card){
        try {
            cards.add(card);
        } catch (Exception e) {
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("Method: setCard\tException: ");
            errorMessage.append(e);
            log.error(errorMessage.toString());
        }
    }


    public synchronized void addDeposit(Transaction item) {
        try {
            deposit.add(item);
        } catch (Exception e) {
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("Method: addDeposit\tException: ");
            errorMessage.append(e);
            log.error(errorMessage.toString());
        }
    }
}
