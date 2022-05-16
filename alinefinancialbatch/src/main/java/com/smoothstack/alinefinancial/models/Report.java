package com.smoothstack.alinefinancial.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name="Report")
@XmlAccessorType(XmlAccessType.FIELD)
public class Report {

    private Long id;

    private String time;

    private HashMap<String, Object> report;
}
