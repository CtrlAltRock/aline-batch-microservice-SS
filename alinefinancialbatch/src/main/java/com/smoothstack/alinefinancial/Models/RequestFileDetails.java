package com.smoothstack.alinefinancial.Models;

import lombok.Data;

@Data
public class RequestFileDetails {

    private String fileNameToProcess;
    private String processedFileName;
    private Boolean persisted;
    private Boolean xml;
    private String fileBack;

}
