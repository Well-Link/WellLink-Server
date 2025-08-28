package com.user.hackathon_v2.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ApiResponse<T> {
    private boolean ok;
    private T data;
    private String errorMessage;
    
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<T>().setOk(true).setData(data);
    }
    
    public static <T> ApiResponse<T> error(String errorMessage) {
        return new ApiResponse<T>().setOk(false).setErrorMessage(errorMessage);
    }
}

@Data
@Accessors(chain = true)
class WelfareListResult {
    private WelfareMeta meta;
    private List<WelfareListItem> items;
}

@Data
@Accessors(chain = true)
class WelfareMeta {
    private String resultCode;
    private String resultMessage;
    private Integer totalCount;
    private Integer pageNo;
    private Integer numOfRows;
}

@Data
@Accessors(chain = true)
class WelfareListItem {
    private String servId;
    private String title;
    private String summary;
    private String link;
    private String lifeArray;
    private String interests;
    private String targets;
}

@Data
@Accessors(chain = true)
class WelfareDetailResult {
    private String servId;
    private String title;
    private String provider;
    private String targetsDetail;
    private String criteria;
    private String benefitContent;
    private String supportCycle;
    private String siteLinks;
    private String contacts;
}