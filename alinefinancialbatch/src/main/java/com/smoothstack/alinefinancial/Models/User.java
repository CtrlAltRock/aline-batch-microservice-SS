package com.smoothstack.alinefinancial.Models;

import lombok.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name="user")
@XmlAccessorType(XmlAccessType.FIELD)
public class User {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private List<Card> cards;
    //private List<Transaction> transactions;
    public void setCard(Card card){
        cards.add(card);
    }

}
