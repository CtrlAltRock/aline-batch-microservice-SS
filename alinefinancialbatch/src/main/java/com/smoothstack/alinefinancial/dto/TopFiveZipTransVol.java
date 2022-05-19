package com.smoothstack.alinefinancial.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name="TopFiveZipTransVol")
@XmlAccessorType(XmlAccessType.FIELD)
@Slf4j(topic = "TopFiveZipTransVol")
public class TopFiveZipTransVol {

    List<ZipTransVol> zipTransVolList = new ArrayList<>();

}
