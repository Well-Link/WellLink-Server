package com.user.hackathon_v2.controller;

import com.user.hackathon_v2.dto.WelfareDetailJsonResponse;
import com.user.hackathon_v2.dto.WelfareDetailResponse;
import com.user.hackathon_v2.dto.WelfareListResponse;
import com.user.hackathon_v2.service.WelfareApiService;
import com.user.hackathon_v2.util.WelfareMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/health")
@RequiredArgsConstructor
@Slf4j
public class WelfareController {
    
    private final WelfareApiService welfareApiService;
    
    @GetMapping("/welfare-list")
    public ResponseEntity<Map<String, Object>> getWelfareList(
            @RequestParam(required = false) Integer age,
            @RequestParam(required = false) String interests,
            @RequestParam(required = false) String targets,
            @RequestParam(required = false) String searchWrd,
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer numOfRows) {
        
        try {
            Map<String, Object> apiParams = new HashMap<>();
            apiParams.put("pageNo", pageNo);
            apiParams.put("numOfRows", numOfRows);
            
            if (age != null) {
                apiParams.put("age", age);
            }
            
            if (searchWrd != null && !searchWrd.trim().isEmpty()) {
                apiParams.put("searchWrd", searchWrd.trim());
            }
            
            String intrsThemaArray = WelfareMapper.mapInterests(interests);
            if (intrsThemaArray != null) {
                apiParams.put("intrsThemaArray", intrsThemaArray);
            }
            
            String trgterIndvdlArray = WelfareMapper.mapTargets(targets);
            if (trgterIndvdlArray != null) {
                apiParams.put("trgterIndvdlArray", trgterIndvdlArray);
            }
            
            WelfareListResponse apiResponse = welfareApiService.getWelfareList(apiParams);
            
            Map<String, Object> response = new LinkedHashMap<>();
            
            if (!"0".equals(apiResponse.getResultCode())) {
                response.put("ok", false);
                Map<String, Object> meta = new LinkedHashMap<>();
                meta.put("resultCode", apiResponse.getResultCode());
                meta.put("resultMessage", apiResponse.getResultMessage());
                response.put("meta", meta);
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response);
            }
            
            response.put("ok", true);
            
            Map<String, Object> meta = new LinkedHashMap<>();
            meta.put("resultCode", apiResponse.getResultCode());
            meta.put("resultMessage", apiResponse.getResultMessage());
            meta.put("totalCount", apiResponse.getTotalCount());
            meta.put("pageNo", apiResponse.getPageNo());
            meta.put("numOfRows", apiResponse.getNumOfRows());
            response.put("meta", meta);
            
            List<Map<String, Object>> items = new ArrayList<>();
            if (apiResponse.getItems() != null) {
                for (WelfareListResponse.WelfareItem item : apiResponse.getItems()) {
                    Map<String, Object> itemMap = new LinkedHashMap<>();
                    itemMap.put("servId", item.getServId());
                    itemMap.put("title", item.getServNm());
                    itemMap.put("summary", item.getServDgst());
                    itemMap.put("link", item.getServDtlLink());
                    itemMap.put("lifeArray", item.getLifeArray());
                    itemMap.put("interests", item.getIntrsThemaArray());
                    itemMap.put("targets", item.getTrgterIndvdlArray());
                    items.add(itemMap);
                }
            }
            response.put("items", items);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("복지서비스 목록 조회 실패", e);
            Map<String, Object> errorResponse = new LinkedHashMap<>();
            errorResponse.put("ok", false);
            errorResponse.put("error", "서버 내부 오류가 발생했습니다");
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(errorResponse);
        }
    }
    
    @GetMapping("/welfare-detail")
    public ResponseEntity<Map<String, Object>> getWelfareDetail(@RequestParam String servId) {
        try {
            WelfareDetailJsonResponse apiResponse = welfareApiService.getWelfareDetail(servId);
            
            Map<String, Object> response = new LinkedHashMap<>();
            
            // JSON 응답에서 resultCode 체크 (0이면 성공)
            if (apiResponse.getResultCode() != null && !"0".equals(apiResponse.getResultCode())) {
                response.put("ok", false);
                response.put("resultCode", apiResponse.getResultCode());
                response.put("resultMessage", apiResponse.getResultMsg());
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response);
            }
            
            response.put("ok", true);
            response.put("servId", apiResponse.getServId());
            response.put("title", apiResponse.getServNm());
            response.put("provider", apiResponse.getJurMnofNm());
            response.put("targetsDetail", apiResponse.getTgtrDtlCn());
            response.put("criteria", apiResponse.getSlctCritCn());
            response.put("benefitContent", apiResponse.getAlwServCn());
            response.put("supportCycle", apiResponse.getSprtCycNm());
            response.put("siteLinks", apiResponse.getInqplHmpgReldList());
            response.put("contacts", apiResponse.getInqplCtadrList());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("복지서비스 상세 조회 실패: servId={}", servId, e);
            Map<String, Object> errorResponse = new LinkedHashMap<>();
            errorResponse.put("ok", false);
            errorResponse.put("error", "서버 내부 오류가 발생했습니다");
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(errorResponse);
        }
    }
    
    @GetMapping("/ping")
    public ResponseEntity<Map<String, Object>> ping() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "OK");
        response.put("message", "복지서비스 API 스모크 테스트용 서버가 정상 동작 중입니다.");
        return ResponseEntity.ok(response);
    }
}