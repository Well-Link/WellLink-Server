package com.user.hackathon_v2.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.util.List;

@Data
@JacksonXmlRootElement(localName = "response")
public class WelfareDetailResponse {
    
    @JacksonXmlProperty(localName = "header")
    private Header header;
    
    @JacksonXmlProperty(localName = "body")
    private Body body;
    
    @JacksonXmlProperty(localName = "servId")
    private String servId;
    
    @Data
    public static class Header {
        @JacksonXmlProperty(localName = "resultCode")
        private String resultCode;
        
        @JacksonXmlProperty(localName = "resultMsg")
        private String resultMsg;
    }
    
    @Data
    public static class Body {
        @JacksonXmlProperty(localName = "items")
        private Items items;
    }
    
    @Data
    public static class Items {
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "item")
        private List<WelfareDetailItem> itemList;
    }
    
    @Data
    public static class WelfareDetailItem {
        @JacksonXmlProperty(localName = "servNm")
        private String servNm;
        
        @JacksonXmlProperty(localName = "jurMnofNm")
        private String jurMnofNm;
        
        @JacksonXmlProperty(localName = "tgtrDtlCn")
        private String tgtrDtlCn;
        
        @JacksonXmlProperty(localName = "slctCritCn")
        private String slctCritCn;
        
        @JacksonXmlProperty(localName = "alwServCn")
        private String alwServCn;
        
        @JacksonXmlProperty(localName = "sprtCycNm")
        private String sprtCycNm;
        
        @JacksonXmlProperty(localName = "inqplHmpgReldList")
        private String inqplHmpgReldList;
        
        @JacksonXmlProperty(localName = "inqplCtadrList")
        private String inqplCtadrList;
    }
}