package com.smoothstack.alinefinancial.Models;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Slf4j(topic = "State")
@XmlRootElement(name="state")
@XmlAccessorType(XmlAccessType.FIELD)
public class State {

    private String name;

    @Id
    private String abbreviation;

    private String capital;

    @ElementCollection
    private List<String> zipCodes;

    public void addZipCodes(String zip) {
        if(!zipCodes.contains(zip)) {
            zipCodes.add(zip);
        }
    }
}
