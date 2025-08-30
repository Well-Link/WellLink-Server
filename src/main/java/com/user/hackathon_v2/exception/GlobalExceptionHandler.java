package com.user.hackathon_v2.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.net.SocketTimeoutException;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<Map<String, Object>> handleResourceAccessException(ResourceAccessException e) {
        log.error("외부 API 연결 실패", e);
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("ok", false);
        response.put("error", "외부 서비스와의 연결이 실패했습니다. 잠시 후 다시 시도해주세요.");
        
        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(response);
    }
    
    @ExceptionHandler(SocketTimeoutException.class)
    public ResponseEntity<Map<String, Object>> handleSocketTimeoutException(SocketTimeoutException e) {
        log.error("외부 API 타임아웃", e);
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("ok", false);
        response.put("error", "외부 서비스 응답 시간이 초과되었습니다. 잠시 후 다시 시도해주세요.");
        
        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(response);
    }
    
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("잘못된 요청 파라미터", e);
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("ok", false);
        response.put("error", "잘못된 요청 파라미터입니다: " + e.getName());
        
        return ResponseEntity.badRequest().body(response);
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("잘못된 요청", e);
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("ok", false);
        response.put("error", e.getMessage());
        
        return ResponseEntity.badRequest().body(response);
    }
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException e) {
        log.error("Runtime 예외 발생", e);
        
        if (e.getMessage() != null && e.getMessage().contains("Service Key")) {
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("ok", false);
            response.put("error", "서비스 키 설정 오류입니다. 관리자에게 문의하세요.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("ok", false);
        response.put("error", "서버 내부 오류가 발생했습니다.");
        
        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(response);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception e) {
        log.error("예상치 못한 예외 발생", e);
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("ok", false);
        response.put("error", "서버에서 예상치 못한 오류가 발생했습니다.");
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}