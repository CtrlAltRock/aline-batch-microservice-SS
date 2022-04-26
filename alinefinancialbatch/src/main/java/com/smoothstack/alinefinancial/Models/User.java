package com.smoothstack.alinefinancial.Models;


import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@XmlRootElement(name="user")
@XmlAccessorType(XmlAccessType.FIELD)
public class User {

    private Long id;

    private String firstName;
    private String lastName;
    private String email;

    private List<Card> cards;

    private List<Transaction> transactions;

    public void setCard(Card card){
        cards.add(card);
    }
    public String writeToXML() {
        return "<User>\n" +
                "<id>" + id + "</id>\n" +
                "<firstName>" + firstName + "</firstName>\n" +
                "<lastName>" + lastName + "</lastName>\n" +
                "<email>" + email + "</email>\n" +
                "<Cards>" + buildCardsXML() + "</Cards>\n" +
                "<Transactions>" + buildTransactionsXML() + "</Transactions>\n"+
                "</User>\n";
    }

    private String buildTransactionsXML() {
        if(transactions == null) return "null";
        StringBuilder transactionStr = new StringBuilder();
        for(Transaction transaction : transactions){
            transactionStr.append("<transaction>");
            transactionStr.append("<card>").append(cards.get(transaction.getCard().intValue())).append("</card>");
            transactionStr.append("<year>").append(transaction.getYear()).append("</year>");
            transactionStr.append("<month>").append(transaction.getMonth()).append("</month>");
            transactionStr.append("<day>").append(transaction.getDay()).append("</day>");
            transactionStr.append("<time>").append(transaction.getTime()).append("</time>");
            transactionStr.append("<amount>").append(transaction.getAmount()).append("</amount>");
            transactionStr.append("<method>").append(transaction.getMethod()).append("</method>");
            transactionStr.append("<Merchant>").append(transaction.getMerchant_name()).append("</Merchant>");
            transactionStr.append("</transaction>");
        }
        return transactionStr.toString();
    }

    private String buildCardsXML() {
        if(cards == null || cards.isEmpty()) return "null";
        StringBuilder cardStr = new StringBuilder();
        for(Card card : cards){
            cardStr.append("<card>").append(card.getNumber()).append("</card>");
        }
        return cardStr.toString();
    }
}
