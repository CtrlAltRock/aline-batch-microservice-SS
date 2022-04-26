package com.smoothstack.alinefinancial.Models;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j(topic = "Card")
@XmlRootElement(name="card")
@XmlAccessorType(XmlAccessType.FIELD)
public class Card {

    private Long id;
    private String number;
    private Long userId;

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", userId=" + userId +
                '}';
    }
}
