package com.smoothstack.alinefinancial.models;

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
}
