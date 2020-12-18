# 통신 방법
1. RestTemplate
2. Fegin

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