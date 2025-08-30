package com.user.hackathon_v2.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "wantedList")
public class WelfareListResponse {
    
    @JacksonXmlProperty(localName = "totalCount")
    private Integer totalCount;
    
    @JacksonXmlProperty(localName = "pageNo")
    private Integer pageNo;
    
    @JacksonXmlProperty(localName = "numOfRows")
    private Integer numOfRows;
    
    @JacksonXmlProperty(localName = "resultCode")
    private String resultCode;
    
    @JacksonXmlProperty(localName = "resultMessage")
    private String resultMessage;
    
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "servList")
    private List<WelfareItem> items;
    
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WelfareItem {
        @JacksonXmlProperty(localName = "servId")
        private String servId;
        
        @JacksonXmlProperty(localName = "servNm")
        private String servNm;
        
        @JacksonXmlProperty(localName = "servDgst")
        private String servDgst;
        
        @JacksonXmlProperty(localName = "servDtlLink")
        private String servDtlLink;
        
        @JacksonXmlProperty(localName = "lifeArray")
        private String lifeArray;
        
        @JacksonXmlProperty(localName = "intrsThemaArray")
        private String intrsThemaArray;
        
        @JacksonXmlProperty(localName = "trgterIndvdlArray")
        private String trgterIndvdlArray;
    }
}