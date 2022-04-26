package com.smoothstack.alinefinancial.Models;


import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
public class MCC {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String code;


    @NotNull
    private String type;
}
