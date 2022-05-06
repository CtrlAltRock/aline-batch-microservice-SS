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
@Slf4j(topic = "State")
@XmlRootElement(name="State")
@XmlAccessorType(XmlAccessType.FIELD)
public class State {

    private String name;

    private String abbreviation;

    private String capital;

    private List<String> zipCodes;

    public void addZipCodes(String zip) {

        try {
            if (!zipCodes.contains(zip)) {
                zipCodes.add(zip);
            }
        } catch (Exception e) {
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("Method: addZipCodes\tException: ");
            errorMessage.append(e);
            log.error(errorMessage.toString());
        }
    }
}
