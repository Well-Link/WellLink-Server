# 복지서비스 API 스모크 테스트 애플리케이션

공공데이터포털의 중앙부처복지서비스 Open API를 활용한 Spring Boot 스모크 테스트 애플리케이션입니다.

## 📋 기능

- **복지서비스 목록 조회**: 나이, 관심분야, 대상별로 복지서비스를 검색
- **복지서비스 상세 조회**: 특정 복지서비스의 상세 정보 조회
- **스모크 테스트**: 애플리케이션 시작 시 자동으로 API 연결 상태 확인

## 🛠 기술 스택

- **Java 17**
- **Spring Boot 3.5.x**
- **Gradle**
- **Jackson XML** (XML 파싱)
- **Lombok**

## 🚀 시작하기

### 1. 환경변수 설정

공공데이터포털에서 발급받은 서비스키를 환경변수로 설정합니다:

```bash
export SERVICE_KEY="<URL-encoded service key>"
```

> ⚠️ **주의**: SERVICE_KEY가 설정되지 않으면 애플리케이션 시작 시 오류가 발생합니다.

### 2. 애플리케이션 실행

```bash
./gradlew bootRun
```

또는 JAR 파일로 실행:

```bash
./gradlew build
java -jar build/libs/hackathon_v2-0.0.1-SNAPSHOT.jar
```

### 3. 스모크 테스트 확인

애플리케이션 시작 시 자동으로 스모크 테스트가 실행되어 API 연결 상태를 확인합니다.

## 📡 API 엔드포인트

### 1. 서버 상태 확인
```bash
curl "http://localhost:8080/health/ping"
```

### 2. 복지서비스 목록 조회
```bash
curl "http://localhost:8080/health/welfare-list?age=22&interests=교육&targets=저소득&pageNo=1&numOfRows=10"
```

**요청 파라미터:**
- `age` (선택): 나이
- `interests` (선택): 관심분야 (콤마 구분)
  - 지원값: `교육`, `일자리`, `주거`, `신체건강`, `정신건강`, `생활지원`, `문화·여가`, `안전·위기`, `임신·출산`, `보육`, `입양·위탁`, `보호·돌봄`, `서민금융`, `법률`, `에너지`
- `targets` (선택): 대상 (콤마 구분)
  - 지원값: `저소득`, `장애인`, `한부모·조손`, `다문화·탈북민`, `다자녀`, `보훈`
- `pageNo` (선택, 기본값: 1): 페이지 번호
- `numOfRows` (선택, 기본값: 10): 페이지당 항목 수

**응답 예시:**
```json
{
  "ok": true,
  "meta": {
    "resultCode": "00",
    "resultMessage": "NORMAL SERVICE.",
    "totalCount": 25,
    "pageNo": 1,
    "numOfRows": 10
  },
  "items": [
    {
      "servId": "WLF0000001234",
      "title": "청년 교육지원 프로그램",
      "summary": "만 18세~29세 청년을 대상으로 하는 교육비 지원",
      "link": "https://...",
      "lifeArray": "청년",
      "interests": "100",
      "targets": "050"
    }
  ]
}
```

### 3. 복지서비스 상세 조회
```bash
curl "http://localhost:8080/health/welfare-detail?servId=WLF0000001234"
```

**요청 파라미터:**
- `servId` (필수): 서비스 ID (목록 조회에서 획득)

**응답 예시:**
```json
{
  "ok": true,
  "servId": "WLF0000001234",
  "title": "청년 교육지원 프로그램",
  "provider": "교육부",
  "targetsDetail": "만 18세 이상 29세 이하 청년",
  "criteria": "소득인정액 기준중위소득 80% 이하",
  "benefitContent": "교육비 최대 200만원 지원",
  "supportCycle": "연 1회",
  "siteLinks": "https://...",
  "contacts": "02-1234-5678"
}
```

## 🔧 설정

### application.yml
```yaml
external:
  api:
    base-url: ${EXTERNAL_API_BASEURL:http://apis.data.go.kr/B554287/NationalWelfareInformationsV001}
    service-key: ${SERVICE_KEY:}

server:
  port: 8080
```

## 🚨 에러 처리

- **502 Bad Gateway**: 외부 API에서 오류 응답 반환
- **504 Gateway Timeout**: 외부 API 연결 실패 또는 타임아웃
- **400 Bad Request**: 잘못된 요청 파라미터
- **500 Internal Server Error**: 서버 내부 오류

## 📝 로깅

애플리케이션 실행 시 다음과 같은 로그를 확인할 수 있습니다:

```
=== 복지서비스 API 스모크 테스트 시작 ===
✅ 스모크 테스트 성공! resultCode: 00
📋 조회된 복지서비스 목록 (상위 3개):
  1. servId: WLF0000001234 | servNm: 청년 교육지원 프로그램
  2. servId: WLF0000001235 | servNm: 저소득층 학습지원사업
  3. servId: WLF0000001236 | servNm: 교육급여
=== 복지서비스 API 스모크 테스트 완료 ===
```

## 🧪 테스트

전체 테스트 실행:
```bash
./gradlew test
```

## 📞 문의

이 애플리케이션은 공공데이터포털의 중앙부처복지서비스 Open API를 활용합니다.
- API 문의: [공공데이터포털](https://www.data.go.kr/)
- 서비스키 발급: 공공데이터포털 회원가입 후 해당 API 신청