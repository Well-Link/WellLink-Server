package com.user.hackathon_v2.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.user.hackathon_v2.dto.WelfareDetailJsonResponse;
import com.user.hackathon_v2.dto.WelfareDetailResponse;
import com.user.hackathon_v2.dto.WelfareListResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.Duration;
import java.util.Map;

@Service
@Slf4j
public class WelfareApiService {
    
    private final RestTemplate restTemplate;
    private final XmlMapper xmlMapper;
    private final ObjectMapper objectMapper;
    private final String baseUrl;
    private final String serviceKey;
    
    public WelfareApiService(RestTemplateBuilder restTemplateBuilder,
                           @Value("${external.api.base-url}") String baseUrl,
                           @Value("${external.api.service-key}") String serviceKey) {
        this.restTemplate = restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(5))
                .setReadTimeout(Duration.ofSeconds(5))
                .build();
        this.xmlMapper = new XmlMapper();
        this.objectMapper = new ObjectMapper();
        this.baseUrl = baseUrl;
        this.serviceKey = serviceKey;
        
        if (serviceKey == null || serviceKey.trim().isEmpty()) {
            throw new IllegalStateException("SERVICE_KEY가 설정되지 않았습니다. 환경변수를 확인해주세요.");
        }
    }
    
    public WelfareListResponse getWelfareList(Map<String, Object> params) {
        try {
            String encodedServiceKey = URLEncoder.encode(serviceKey, "UTF-8");
            
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + "/NationalWelfarelistV001")
                    .queryParam("serviceKey", encodedServiceKey)
                    .queryParam("callTp", "L")
                    .queryParam("srchKeyCode", "003")  // 필수 파라미터 추가
                    .queryParam("orderBy", "popular");
            
            params.forEach((key, value) -> {
                if (value != null) {
                    builder.queryParam(key, value);
                }
            });
            
            String url = builder.toUriString();
            log.info("복지서비스 목록 조회 API 호출: {}", url.substring(0, Math.min(url.length(), 200)) + "...");
            
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            log.info("API 응답 상태: {}", response.getStatusCode());
            log.info("API 응답 내용: {}", response.getBody());
            
            WelfareListResponse result = xmlMapper.readValue(response.getBody(), WelfareListResponse.class);
            log.info("파싱된 결과 - resultCode: {}, totalCount: {}", result.getResultCode(), result.getTotalCount());
            return result;
            
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Service Key 인코딩 실패", e);
        } catch (Exception e) {
            log.error("복지서비스 목록 조회 중 오류 발생", e);
            throw new RuntimeException("외부 API 호출 중 오류가 발생했습니다", e);
        }
    }
    
    public WelfareDetailJsonResponse getWelfareDetail(String servId) {
        try {
            String encodedServiceKey = URLEncoder.encode(serviceKey, "UTF-8");
            
            String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/NationalWelfaredetailedV001")
                    .queryParam("serviceKey", encodedServiceKey)
                    .queryParam("callTp", "D")
                    .queryParam("servId", servId)
                    .toUriString();
            
            log.info("복지서비스 상세 조회 API 호출: servId={}", servId);
            
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            log.info("API 응답 상태: {}", response.getStatusCode());
            log.info("API 응답 내용: {}", response.getBody());
            
            // XML 응답을 파싱하여 DTO로 변환
            WelfareDetailResponse xmlResponse = xmlMapper.readValue(response.getBody(), WelfareDetailResponse.class);
            
            // XML 응답을 JSON DTO로 변환 (모든 데이터가 루트 레벨에 있음)
            WelfareDetailJsonResponse jsonResponse = new WelfareDetailJsonResponse();
            jsonResponse.setServId(xmlResponse.getServId());
            jsonResponse.setServNm(xmlResponse.getServNm());
            jsonResponse.setJurMnofNm(xmlResponse.getJurMnofNm());
            jsonResponse.setTgtrDtlCn(xmlResponse.getTgtrDtlCn());
            jsonResponse.setSlctCritCn(xmlResponse.getSlctCritCn());
            jsonResponse.setAlwServCn(xmlResponse.getAlwServCn());
            jsonResponse.setSprtCycNm(xmlResponse.getSprtCycNm());
            jsonResponse.setInqplHmpgReldList(xmlResponse.getInqplHmpgReldList());
            jsonResponse.setInqplCtadrList(xmlResponse.getInqplCtadrList());
            
            // 에러 코드가 있다면 설정
            if (xmlResponse.getHeader() != null) {
                jsonResponse.setResultCode(xmlResponse.getHeader().getResultCode());
                jsonResponse.setResultMsg(xmlResponse.getHeader().getResultMsg());
            }
            
            return jsonResponse;
            
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Service Key 인코딩 실패", e);
        } catch (Exception e) {
            log.error("복지서비스 상세 조회 중 오류 발생: servId={}", servId, e);
            throw new RuntimeException("외부 API 호출 중 오류가 발생했습니다", e);
        }
    }
}