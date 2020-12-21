# 원본소스 주소 
```
git clone https://kimyongyeon@bitbucket.org/kimyongyeon/twd-bff-app.git
```

# Controller 템플릿 
```
@Profile(value = {"local", "default"})
@RequestMapping("/api/v1/sample")
@RestController
@Api(value="SampleController", tags = "샘플 테스트 APIs") // API 스펙 설정 정의
public class SampleController {

    @Autowired
    SampleService sampleService;

    // GET - 기본 
    @ApiOperation(value = "get", notes = "sample get 함수 입니다.") // API 메서드 제목,설명 정의
    @ApiImplicitParams({ // API 메서드 파라미터 상세설명, GET 메서드에서만 사용 
            @ApiImplicitParam(name = "name", value="이름", dataType = "String", paramType="query"),
    })
    @GetMapping(value = "/get" /* URL 주소 */ 
        , consumes = MediaType.APPLICATION_JSON_VALUE /* 요청 페이로드 타입 제한하기 */ 
        , produces = MediaType.APPLICATION_JSON_VALUE /* 응답 데이터 제한하기 */ )
    public ApiMessageVO get(String name) {
        Map map = new HashMap();
        map.put("name", name);
        return ApiMessageVO.builder()
                .resMsg(OK_RESP_MSG)
                .respBody(sampleService.get(map))
                .respCode(OK_RESP_CODE)
                .build();
    }
    
    // GET - Header로 값을 받는 경우 
    @ApiOperation(value = "getNameMaking", notes = "sample getNameMaking 함수 입니다.", response = SampleVO.class)
    @GetMapping("/maskingName")
    public ApiMessageVO getNameMaking(
            @RequestHeader(value="mdn") String mdn,
            @RequestHeader(value="prodNo") String prodNo,
            @ApiParam(value = "SampleVO 파라미터", required = true) SampleVO sampleVO) throws Exception {
    
        MaskingHelper.init(CommonConstant.LANG_CD);
        String nameType = MaskingTypeEnum.MaskName.getCode();
        String nameMaskingStr = MaskingHelper.maskString(nameType, sampleVO.getName());
    
        return ApiMessageVO.builder()
                .resMsg(OK_RESP_MSG)
                .respBody(SampleVO.builder().name(nameMaskingStr).build())
                .respCode(OK_RESP_CODE)
                .build();
    }
    
    // POST
    @ApiOperation(value = "post", notes = "sample post 함수 입니다.", response = SampleVO.class)
    @PostMapping(name = "/post")
    public ApiMessageVO post(@RequestBody SampleVO sampleVO) {
        Map map = new HashMap();
        map.put("prodId", sampleVO.getName() + "::post");
        map.put("mdn", sampleVO.getTel());
        return ApiMessageVO.builder()
                .resMsg(OK_RESP_MSG)
                .respBody(sampleService.post(map))
                .respCode(OK_RESP_CODE)
                .build();
    }

    // PUT
    @ApiOperation(value = "put", notes = "sample put 함수 입니다.", response = SampleVO.class)
    @PutMapping(name = "/put")
    public ApiMessageVO put(@RequestBody SampleVO sampleVO) {
        Map map = new HashMap();
        map.put("prodId", sampleVO.getName() + "::put");
        map.put("mdn", sampleVO.getTel());
        return ApiMessageVO.builder()
                .resMsg(OK_RESP_MSG)
                .respBody(sampleService.put(map))
                .respCode(OK_RESP_CODE)
                .build();
    }
    
    // Cache 키 저장  
    @Cacheable(value = CacheKey.SAMPLE, unless = "#result == null", cacheManager = "cacheManager")
    @ApiOperation(value = "getNameMaking", notes = "sample getNameMaking 함수 입니다.", response = SampleVO.class)
    @GetMapping("/maskingName")
    public ApiMessageVO getNameMaking(
            @RequestHeader(value="mdn") String mdn,
            @RequestHeader(value="prodNo") String prodNo,
            @ApiParam(value = "SampleVO 파라미터", required = true) SampleVO sampleVO) throws Exception {

        MaskingHelper.init(CommonConstant.LANG_CD);
        String nameType = MaskingTypeEnum.MaskName.getCode();
        String nameMaskingStr = MaskingHelper.maskString(nameType, sampleVO.getName());

        return ApiMessageVO.builder()
                .resMsg(OK_RESP_MSG)
                .respBody(SampleVO.builder().name(nameMaskingStr).build())
                .respCode(OK_RESP_CODE)
                .build();
    }
    
    // Cache 키 삭제 
    @CacheEvict(value = CacheKey.SAMPLE)
    @ApiOperation(value = "del", notes = "sample del 함수 입니다.", response = SampleVO.class)
    @DeleteMapping(name = "/del")
    public ApiMessageVO del(@RequestBody SampleVO sampleVO) {
        Map map = new HashMap();
        map.put("prodId", sampleVO.getName() + "::del");
        map.put("mdn", sampleVO.getTel());
        return ApiMessageVO.builder()
                .resMsg(OK_RESP_MSG)
                .respBody(sampleService.del(map))
                .respCode(OK_RESP_CODE)
                .build();
    }
}
``` 

# Service 탬플릿
```
public interface SampleService {

    String get(Map param); // GET 메서드 샘플
    String post(Map param); // POST 메서드 샘플 
    String del(Map param); // DELETE 메서드 샘플
    String put(Map param); // PUT 메서드  샘플

    String hello(String name); // Feign 메서드 샘플

    @Service
    @Slf4j
    class SampleServiceImpl implements SampleService {

        @Autowired
        RestBackendAPI restBackendAPI;

        @Autowired
        SampleFeign sampleFeign;

        public String hello(String name) {
            return sampleFeign.hello(name);
        }

        @Override
        public String get(Map param) {
            RestBackendApiHeadersVO.url = "http://localhost:10011/api/v1/sample/hello";
            RestBackendApiHeadersVO.key = "test";
            RestBackendApiHeadersVO.value = "test12345";
            ResponseEntity<?> responseEntity = restBackendAPI.httpGet(RestBackendApiHeadersVO.url, param);
            log.debug("respBody: " + responseEntity.getBody());
            Map resultMap = (Map) responseEntity.getBody();
            return resultMap.get("respBody").toString();
        }

        @Override
        public String post(Map param) {
            RestBackendApiHeadersVO.url = "http://localhost:10011/api/v1/sample/helloPost";
            HttpEntity<?> httpEntity = restBackendAPI.httpPost(RestBackendApiHeadersVO.url, param);
            return httpEntity.toString();
        }

        @Override
        public String del(Map param) {
            RestBackendApiHeadersVO.url = "http://localhost:10011/api/v1/sample/helloPost";
            HttpEntity<?> httpEntity = restBackendAPI.httpDel(RestBackendApiHeadersVO.url, param);
            return httpEntity.getBody().toString();
        }

        @Override
        public String put(Map param) {
            RestBackendApiHeadersVO.url = "http://localhost:10011/api/v1/sample/helloPost";
            HttpEntity<?> httpEntity = restBackendAPI.httpPut(RestBackendApiHeadersVO.url, param);
            return httpEntity.toString();
        }
    }
}

```

# 권장 툴
- intellij: https://www.jetbrains.com/ko-kr/idea/
- STS: https://spring.io/tools
- visual studio code: https://code.visualstudio.com/download

# 도움되는 사이트 
- json editor: https://jsoneditoronline.org/#left=local.soyiro&right=local.fawige
- thymeleaf: https://www.thymeleaf.org/
- 변수명이름짓기: https://www.curioustore.com/#!/


# frontend swagger-ui 
http://localhost:10011/swagger-ui.html

# block-url
http://localhost:10011/block/hidden-key

# 프로젝트 최초 구성시
## 개발환경
- https://adoptopenjdk.net/?variant=openjdk15&jvmVariant=hotspot
- JDK: openJDK8
- JVM: OpenJ9
- lombok
 
## application.yml 제목, 타이틀 변경
```
application:
  name: Tworld Direct Bff Project
  title: Tworld Direct Project (sub-service-name)  <= 최초수정 
  formatted-version: 0.0.1

server:
  port: 10011

spring:
  profiles:
      active: default
  application:
    name: Tworld_Direct_Bff_Project <= 최초수정
```

## pom.xml 제목 수정
groupId, artifactId, name, description 내용을 컴포넌트 특징에 맞게 변경 

```
<groupId>com.twd.bff.app</groupId> 
<artifactId>twd-bff-app</artifactId>
<name>twd-bff-app</name>
<description>Tworld Direct Backend Front App</description>
```

## banner.txt 제목 변경
`아래 사이트를 이동후 제목을 만들어 넣어주세면 됩니다.`
http://patorjk.com/software/taag/#p=display&f=Calvin%20S&t=TWD%20BFF%20PROJECT

```   
${Ansi.YELLOW}***********************************************************************
${Ansi.GREEN} ╔╦╗╦ ╦╔╦╗  ╔╗ ╔═╗╔═╗  ╔═╗╦═╗╔═╗ ╦╔═╗╔═╗╔╦╗
${Ansi.GREEN}  ║ ║║║ ║║  ╠╩╗╠╣ ╠╣   ╠═╝╠╦╝║ ║ ║║╣ ║   ║
${Ansi.GREEN}  ╩ ╚╩╝═╩╝  ╚═╝╚  ╚    ╩  ╩╚═╚═╝╚╝╚═╝╚═╝ ╩
${Ansi.GREEN}Application                    : ${application.name}
${Ansi.GREEN}Port                           : ${server.port}
${Ansi.GREEN}Active                         : ${spring.profiles.active}
${Ansi.GREEN}Application Version            : ${application.formatted-version}
${Ansi.GREEN}Application Title              : ${application.title}
${Ansi.GREEN}Spring Boot Version            : ${spring-boot.version}
${Ansi.GREEN}Spring Boot Formatted Version  : ${spring-boot.formatted-version}
${Ansi.YELLOW}***********************************************************************
```

# 샘플코드 위치 
- controller: src\main\java\com\twd\bff\app\biz\controller\SampleController.java
- service: src\main\java\com\twd\bff\app\biz\service\SampleService.java
- feign: src\main\java\com\twd\bff\app\biz\feign\SampleFeign.java
- vo: src\main\java\com\twd\bff\app\biz\vo\SampleVO.java

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

# 주석방법
```
1. 메서드 
/**
 * @title makingName
 * @param mdn
 * @param prodNo
 * @param sampleVO
 * @return
 * @throws Exception
 */

2. 한줄 주석 
int number = 0; // 초기화는 반드시 해야 합니다.

3. 여러줄 주석 
/*
숫자를 뽑을때는 다음 함수를 사용해야 한다.
이유는 중앙에서 Generator 하고 있다.
*/
int getNumber();

```

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

# CircuitBreaker(회복탄력성) 설정 설명
- coreSize: 100 `default:10 - 코어(기본) thread pool 크기`
- maximumSize: 500 `default:15 - 최대 thread pool 크기. allowMaximumSizeToDivergeFromCoreSize 값이 true여야 유효하다.`
- timeoutInMilliseconds: 3000 `default:300ms - Thread 사용시 - Command 쓰레드를 호출한 Caller 입장에서의 타임아웃 값`
- maxConcurrentRequests: 20 `Call thread에서 호출할 수 있는 HystrixCommand.getFallback()에 대한 요청의 최대 수`
- requestVolumeThreshold: 2 `Rolling Window 구간에 대한 request 처리 건수의 최소값. 이 값에 도달하기 전에 발생한 fail에 대해서는 circuit을 open 하지 않음`
- errorThresholdPercentage: 50    `default:50 - 이 설정값 이상의 에러율이 발생하면 circuit을 쇼트시킴`

# 패키지 구조 
- com.twd.bff.app: root 패키지
- com.twd.bff.app.biz: 업무 컴포넌트 패키지
- com.twd.bff.app.common: 공통 컴포넌트 패키지
- com.twd.bff.app.config: 공통 설정 패키지 

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

# Redis Cacheable 설정
- 기본설정: 1분
- 객체: 자유롭게 설정

# 유틸
## 마스킹 사용예시

```
MaskingHelper.init(Constants.LANG_CD);

// 생년월일 
String birthType = MaskingTypeEnum.MaskBirth.getCode();
String birthMaskingStr = MaskingHelper.maskString(birthType, mapresult.get("birth").toString());

// mdn 코드 
String mdnType = MaskingTypeEnum.MaskCellOne.getCode();
String mdnMaskingStr = MaskingHelper.maskString(mdnType, mapresult.get("mdn").toString());

// 이름 
String nameType = MaskingTypeEnum.MaskName.getCode();
String nameMaskingStr = MaskingHelper.maskString(nameType, mapresult.get("name").toString());

// 이메일 
String emailType = MaskingTypeEnum.MaskEmail.getCode();
String emailMaskingStr = MaskingHelper.maskString(emailType, mapresult.get("email_addr").toString());

```

# 스프링부트 시작
```
//////////////////////////////////////////////////////////////////
# 테스트 스킵 jar 생성 방법
//////////////////////////////////////////////////////////////////

1. mvnw clean install -DskipTests
2. mvnw clean install -Dmaven.test.skip=true

//////////////////////////////////////////////////////////////////
# 스프링 시작 방법 
//////////////////////////////////////////////////////////////////

mvnw spring-boot:run
java -jar target/twd-bff-app-0.0.1-SNAPSHOT.jar -Dspring.profiles.active=local

//////////////////////////////////////////////////////////////////
# 프로파일 주입 방식
//////////////////////////////////////////////////////////////////

##################################################################
1. WebApplicationInitializer 인터페이스를 통한 방법
##################################################################
@Configuration
public class MyWebApplicationInitializer 
  implements WebApplicationInitializer {
 
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
  
        servletContext.setInitParameter(
          "spring.profiles.active", "dev");
    }
}

##################################################################
2. ConfigurableEnvironment 를 통한 방법
##################################################################
@Autowired
private ConfigurableEnvironment env;
env.setActiveProfiles("someProfile");

##################################################################
3. JVM 파라미터를 통한 방법
##################################################################
-Dspring.profiles.active=dev

##################################################################
4. 환경변수를 통한 방법
##################################################################
export spring_profiles_active=dev

##################################################################
5. Maven profiles를 통한 방법
##################################################################
<profiles>
    <profile>
        <id>dev</id>
        <activation>
            <activeByDefault>true</activeByDefault>
        </activation>
        <properties>
            <spring.profiles.active>dev</spring.profiles.active>
        </properties>
    </profile>
    <profile>
        <id>prod</id>
        <properties>
            <spring.profiles.active>prod</spring.profiles.active>
        </properties>
    </profile>
</profiles>

##################################################################
6. 메이븐 방식 -P parameter 방법
##################################################################
mvn clean package -Plocal

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
│  │  │                  │  └─v1
│  │  │                  │      └─sample
│  │  │                  │          ├─controller
│  │  │                  │          ├─feign
│  │  │                  │          ├─service
│  │  │                  │          └─vo
│  │  │                  ├─common
│  │  │                  │  ├─block
│  │  │                  │  ├─constant
│  │  │                  │  ├─exception
│  │  │                  │  ├─logging
│  │  │                  │  ├─util
│  │  │                  │  │  └─masking
│  │  │                  │  └─vo
│  │  │                  └─config
│  │  │                      └─redis
│  │  └─resources
│  │      ├─encryption
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