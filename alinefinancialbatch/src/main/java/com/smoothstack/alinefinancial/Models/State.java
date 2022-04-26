package com.smoothstack.alinefinancial.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name="state")
@XmlAccessorType(XmlAccessType.FIELD)
public class State {

    private String name;

    private String abbreviation;

    private String capital;

    private List<String> zipCodes;

    public void addZipCodes(String zip) {
        if(!zipCodes.contains(zip)) {
            zipCodes.add(zip);
        }
    }
}
