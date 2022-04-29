package com.smoothstack.alinefinancial.Models;

import lombok.*;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name="user")
@XmlAccessorType(XmlAccessType.FIELD)
public class User {

    @Id
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    @ManyToMany
    private List<Card> cards;

    //Not using this for now, may end up using later for analysis
    //private List<Transaction> transactions;

    public void setCard(Card card){
        cards.add(card);
    }


}
