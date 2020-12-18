# 통신 방법
## 1. RestTemplate
### header 설정방법
```
RestBackendApiHeadersVO.key = "test";
RestBackendApiHeadersVO.value = "test12345";
ResponseEntity<?> responseEntity = restBackendAPI.httpGet("/api/v1/sample/hello", new HashMap<>());
```

### map 설정방법
```
Map paramMap = new HashMap();
paramMap.put("key","value");
ResponseEntity<?> responseEntity = restBackendAPI.httpGet("/api/v1/sample/hello", paramMap);
```

### 메서드별 호출방법
```
1. GET 메서드
restBackendAPI.httpGet("/api/v1/sample/hello", new HashMap<>());
2. POST 메서드
restBackendAPI.httpPost("/api/v1/sample/hello", new HashMap<>());
3. PUT 메서드
restBackendAPI.httpPut("/api/v1/sample/hello", new HashMap<>());
4. DELETE 메서드
restBackendAPI.httpDel("/api/v1/sample/hello", new HashMap<>());
```

## 2. Fegin

# 로깅
`프로파일별로 처리 되어 있다.`

- default, local: info 레벨 출력
- dev: debug 레벨 출력
- prd: error 레벨 출력

# application 설정파일 설명
- application.yml: 공통 설정 부분 정의
- application-default: 프로파일을 정의하지 않으면 default 환경으로 구동된다.
- application-local: 로컬에서 본인만 특별하게 설정하여 처리해야 할때 사용.
- application-prd: 운영환경으로 설정해야 하는 정보를 입력하여 사용해야 할때 사용.

# 서킷브레이크 설정 설명
- coreSize: 100 `default:10     - 코어(기본) thread pool 크기`
- maximumSize: 500 `default:15     - 최대 thread pool 크기. allowMaximumSizeToDivergeFromCoreSize 값이 true여야 유효하다.`
- timeoutInMilliseconds: 3000 `default:300ms  - Thread 사용시 - Command 쓰레드를 호출한 Caller 입장에서의 타임아웃 값`
- maxConcurrentRequests: 20 `Call thread에서 호출할 수 있는 HystrixCommand.getFallback()에 대한 요청의 최대 수`
- requestVolumeThreshold: 2 `Rolling Window 구간에 대한 request 처리 건수의 최소값. 이 값에 도달하기 전에 발생한 fail에 대해서는 circuit을 open 하지 않음`

# 패키지 구조 
- com.twd.bff: root 패키지
- com.twd.bff.biz: 업무 컴포넌트 패키지
- com.twd.bff.common: 공통 컴포넌트 패키지
- com.twd.bff.config: 공통 설정 패키지 

# 공통 설정 설명
- MvcConfiguration: MVC 모델 관련 설정 정보 
- RedisConfig: 레디스 설정 정보
- RestTemplateConfig: RestTemplate 설정 정보, 커넥션풀, 타임설정 등.
- ServiceConfig: Jackson2Object 설정 정보
- SwaggerConfig: 스웨거 문서관리 미들웨어 설정 정보
- BasicAuthConfiguration: 베이직 인증 설정 정보. 
- FeignRetryConfiguration: 페인 리트라이 설정 정보.

# RestTemplate 설정 설명
- readTime: 3000 `소켓통신 상태 3초`
- connTimeout: 3000 `연결시도 3초`
- connectionMaxTotal: 200 `연결유지 최대 개수`
    - 참고: Tomcat Heap Size Issue 발생시 커넥션수를 줄여라. 또는 Tomcat Heap 메모리 크기를 늘려줘라
- defaultMaxPerRoute: 20 `어떤 경로든 최대 연결수`
- maxPerRoute: 50 `전달되는 경로에 대해서 최대 연결 갯수, defaultMaxPerRoute 무시함`

# 향후 목표
- 집킨, 슬루스 서버 나오면 연결
- 레디스 연동 
- block 패키지 관련해서 빠진 패키지들이 있어 일단 주서처리 하였음.

# 스프링부트 시작
```
# 테스트 스킵  
1. mvnw clean install -DskipTests
2. mvnw clean install -Dmaven.test.skip=true

# 스프링 시작 
mvnw spring-boot:run
java -jar target/twd-bff-app-0.0.1-SNAPSHOT.jar

```

# 폴더구조
```
F:.
├─src
│  ├─main
│  │  ├─java
│  │  │  └─com
│  │  │      └─twd
│  │  │          └─bff
│  │  │              └─app
│  │  │                  ├─biz
│  │  │                  │  ├─controller
│  │  │                  │  ├─feign
│  │  │                  │  ├─service
│  │  │                  │  └─vo
│  │  │                  ├─common
│  │  │                  │  ├─constant
│  │  │                  │  ├─exception
│  │  │                  │  ├─logging
│  │  │                  │  ├─util
│  │  │                  │  └─vo
│  │  │                  └─config
│  │  └─resources
│  │      ├─logger
│  │      │  └─logback
│  │      ├─static
│  │      └─templates
│  └─test
│      └─java
│          └─com
│              └─twd
│                  └─bff
│                      └─app
```