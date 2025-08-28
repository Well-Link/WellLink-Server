package com.user.hackathon_v2.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WelfareDetailJsonResponse {
    private String servId;
    private String servNm;
    private String jurMnofNm;
    private String tgtrDtlCn;
    private String slctCritCn;
    private String alwServCn;
    private String sprtCycNm;
    private String inqplHmpgReldList;
    private String inqplCtadrList;
    private String resultCode;
    private String resultMsg;
}