package com.user.hackathon_v2.config;

import com.user.hackathon_v2.dto.WelfareListResponse;
import com.user.hackathon_v2.service.WelfareApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class SmokeTestRunner implements CommandLineRunner {
    
    private final WelfareApiService welfareApiService;
    
    @Override
    public void run(String... args) throws Exception {
        log.info("=== 복지서비스 API 스모크 테스트 시작 ===");
        
        try {
            Map<String, Object> testParams = new HashMap<>();
            testParams.put("pageNo", 1);
            testParams.put("numOfRows", 3);
            testParams.put("age", 22);
            testParams.put("intrsThemaArray", "100");
            
            WelfareListResponse response = welfareApiService.getWelfareList(testParams);
            
            if ("0".equals(response.getResultCode())) {
                log.info("✅ 스모크 테스트 성공! resultCode: {}", response.getResultCode());
                
                if (response.getItems() != null) {
                    log.info("📋 조회된 복지서비스 목록 (상위 3개):");
                    
                    for (int i = 0; i < Math.min(3, response.getItems().size()); i++) {
                        WelfareListResponse.WelfareItem item = response.getItems().get(i);
                        log.info("  {}. servId: {} | servNm: {}", 
                            (i + 1), item.getServId(), item.getServNm());
                    }
                }
            } else {
                log.warn("⚠️ 스모크 테스트 실패 - resultCode: {}, message: {}", 
                    response.getResultCode(), 
                    response.getResultMessage());
            }
            
        } catch (Exception e) {
            log.warn("⚠️ 스모크 테스트 중 예외 발생: {}", e.getMessage());
            log.debug("스모크 테스트 예외 상세:", e);
        }
        
        log.info("=== 복지서비스 API 스모크 테스트 완료 ===");
        log.info("💡 테스트용 엔드포인트:");
        log.info("  - GET /health/ping");
        log.info("  - GET /health/welfare-list?age=22&interests=교육&targets=저소득");
        log.info("  - GET /health/welfare-detail?servId=<servId>");
    }
}