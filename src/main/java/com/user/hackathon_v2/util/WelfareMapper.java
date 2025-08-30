package com.user.hackathon_v2.util;

import java.util.HashMap;
import java.util.Map;

public class WelfareMapper {
    
    private static final Map<String, String> INTERESTS_MAP = new HashMap<>();
    private static final Map<String, String> TARGETS_MAP = new HashMap<>();
    
    static {
        INTERESTS_MAP.put("교육", "100");
        INTERESTS_MAP.put("일자리", "050");
        INTERESTS_MAP.put("주거", "040");
        INTERESTS_MAP.put("신체건강", "010");
        INTERESTS_MAP.put("정신건강", "020");
        INTERESTS_MAP.put("생활지원", "030");
        INTERESTS_MAP.put("문화·여가", "060");
        INTERESTS_MAP.put("문화여가", "060");
        INTERESTS_MAP.put("안전·위기", "070");
        INTERESTS_MAP.put("안전위기", "070");
        INTERESTS_MAP.put("임신·출산", "080");
        INTERESTS_MAP.put("임신출산", "080");
        INTERESTS_MAP.put("보육", "090");
        INTERESTS_MAP.put("입양·위탁", "110");
        INTERESTS_MAP.put("입양위탁", "110");
        INTERESTS_MAP.put("보호·돌봄", "120");
        INTERESTS_MAP.put("보호돌봄", "120");
        INTERESTS_MAP.put("서민금융", "130");
        INTERESTS_MAP.put("법률", "140");
        INTERESTS_MAP.put("에너지", "160");
        
        TARGETS_MAP.put("저소득", "050");
        TARGETS_MAP.put("장애인", "040");
        TARGETS_MAP.put("한부모·조손", "060");
        TARGETS_MAP.put("한부모조손", "060");
        TARGETS_MAP.put("다문화·탈북민", "010");
        TARGETS_MAP.put("다문화탈북민", "010");
        TARGETS_MAP.put("다자녀", "020");
        TARGETS_MAP.put("보훈", "030");
    }
    
    public static String mapInterests(String interests) {
        if (interests == null || interests.trim().isEmpty()) {
            return null;
        }
        
        String[] items = interests.split(",");
        StringBuilder result = new StringBuilder();
        
        for (String item : items) {
            String trimmed = item.trim().toLowerCase();
            String code = INTERESTS_MAP.get(trimmed);
            if (code != null) {
                if (result.length() > 0) {
                    result.append(",");
                }
                result.append(code);
            }
        }
        
        return result.length() > 0 ? result.toString() : null;
    }
    
    public static String mapTargets(String targets) {
        if (targets == null || targets.trim().isEmpty()) {
            return null;
        }
        
        String[] items = targets.split(",");
        StringBuilder result = new StringBuilder();
        
        for (String item : items) {
            String trimmed = item.trim().toLowerCase();
            String code = TARGETS_MAP.get(trimmed);
            if (code != null) {
                if (result.length() > 0) {
                    result.append(",");
                }
                result.append(code);
            }
        }
        
        return result.length() > 0 ? result.toString() : null;
    }
}