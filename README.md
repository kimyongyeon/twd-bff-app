# 통신 방법
## 1. RestTemplate
### header 설정방법
```$xslt
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
# GET 메서드
restBackendAPI.httpGet("/api/v1/sample/hello", new HashMap<>());
# POST 메서드
restBackendAPI.httpPost("/api/v1/sample/hello", new HashMap<>());
# PUT 메서드
restBackendAPI.httpPut("/api/v1/sample/hello", new HashMap<>());
# DELETE 메서드
restBackendAPI.httpDel("/api/v1/sample/hello", new HashMap<>());
```

## 2. Fegin

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