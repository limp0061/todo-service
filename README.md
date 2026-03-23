# todo-service

- Spring Boot 기반의 일정 관리 서비스 REST API

## 기술 스택

- JAVA 17, Spring Boot 3.x
- Spring Data JPA, H2 Database, QueryDSL
- Docker, Redis
- JWT, Lombok, Spring Validation

## 주요 기능

- JWT 기반 회원 인증/인가
- Todo 일정 CRUD (반복 일정 지원)
- 태그 CRUD 및 Todo 태그 연결
- Todo 기간별 완료율 통계
- 월별 캘린더 뷰 (날짜별 할일 수)
- 오늘 할일 목록 Redis 캐싱

## 반복 일정 정책

| 반복 타입        | 최대 반복 기간 |
|--------------|----------|
| 매일 (DAILY)   | 3개월      |
| 매주 (WEEKLY)  | 6개월      |
| 매달 (MONTHLY) | 1년       |
| 매년 (YEARLY)  | 3년       |

## 트러블 슈팅

### 1. @RequestParam으로 enum 못 받는 문제

- **문제**: ?priority=low 요청 시 400 에러
- **원인**: @JsonCreator는 RequestBody(JSON)에서만 동작하고
  URL 파라미터는 별도 변환이 필요
- **해결**: StringToCodeEnumConverterFactory 추가

### 2. @ModelAttribute에서 LocalDate 변환 안 되는 문제

- **문제**: startDate, endDate 파라미터 전달해도 null로 들어옴
- **원인**: URL 파라미터의 날짜 문자열을 LocalDate로 변환하려면
  @DateTimeFormat이 필요
- **해결**: @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) 추가

### 3. Controller마다 HttpServletRequest로 memberId 반복 추출 문제

- **문제**: 모든 Controller 메서드에서 `HttpServletRequest`로 memberId를 꺼내는 코드가 반복
- **원인**: JWT Filter에서 setAttribute로 넣은 값을 꺼내는 공통로직이 없음
- **해결**: `@LoginId` 어노테이션과 `LoginIdArgumentResolver` 추가하여 파라미터에 자동 주입

## 실행 방법

> `.env.example` 파일을 참고하여 `JWT_SECRET` 환경변수를 설정.

```bash
cd docker
docker compose up -d

./gradlew clean build
./gradlew bootRun 
```

## API

Postman Collection을 Import해서 사용.

```
src/main/resources/docs/[Todo] api-practice.postman_collection.json
src/main/resources/docs/auth-environment.postman_environment.json
```

> Postman → Import → 파일 선택
