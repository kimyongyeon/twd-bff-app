package com.twd.bff.app.biz.v1.sample.controller;

import com.twd.bff.app.biz.v1.sample.service.SampleService;
import com.twd.bff.app.biz.v1.sample.vo.SampleVO;
import com.twd.bff.app.common.constant.CommonConstant;
import com.twd.bff.app.common.util.masking.MaskingHelper;
import com.twd.bff.app.common.util.masking.MaskingTypeEnum;
import com.twd.bff.app.common.vo.ApiMessageVO;
import com.twd.bff.app.config.redis.CacheKey;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import static com.twd.bff.app.common.constant.CommonConstant.OK_RESP_CODE;
import static com.twd.bff.app.common.constant.CommonConstant.OK_RESP_MSG;

@Profile(value = {"local", "default"})
@RequestMapping("/api/v1/sample")
@RestController
@Api(value="SampleController", tags = "샘플 테스트 APIs")
public class SampleController {

    @Autowired
    CacheManager cacheManager;

    @Autowired
    SampleService sampleService;

    @ApiOperation(value = "get", notes = "sample get 함수 입니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value="이름", dataType = "String", paramType="query"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK !!"),
            @ApiResponse(code = 500, message = "Internal Server Error !!"),
            @ApiResponse(code = 404, message = "Not Found !!")
    })
    @GetMapping("/get")
    public ApiMessageVO get(String name) {

        // 캐시 초기화
        cacheManager.getCache("common-classes").evict("name:" + name + ":classes");

        Map map = new HashMap();
        map.put("name", name);
        return ApiMessageVO.builder()
                .resMsg(OK_RESP_MSG)
                .respBody(sampleService.get(map))
                .respCode(OK_RESP_CODE)
                .build();
    }

    @ApiOperation(value = "feignTest", notes = "sample feignTest 함수 입니다.")
    @GetMapping("/feignTest")
    public ApiMessageVO feignTest(String name) {
        return ApiMessageVO.builder()
                .resMsg(OK_RESP_MSG)
                .respBody(sampleService.hello(name))
                .respCode(OK_RESP_CODE)
                .build();
    }

    @ApiOperation(value = "feignHello", notes = "sample feignHello 함수 입니다.")
    @GetMapping("/feignHello")
    public ApiMessageVO feignHello(String name) {
        return ApiMessageVO.builder()
                .resMsg(OK_RESP_MSG)
                .respBody("feignHello: " + name)
                .respCode(OK_RESP_CODE)
                .build();
    }


    @ApiOperation(value = "hello", notes = "sample hello 함수 입니다.")
    @GetMapping(value = "/hello")
    public ApiMessageVO hello() {
        return ApiMessageVO.builder()
                .resMsg(OK_RESP_MSG)
                .respBody("hello")
                .respCode(OK_RESP_CODE)
                .build();
    }

    @ApiOperation(value = "helloPost", notes = "sample helloPost 함수 입니다.")
    @RequestMapping(value = "helloPost"
            , method = {RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT}
            /* 요청 페이로드 타입 제한하기, 요청 헤더에 반드시 application/json 이 존재해야한다. */
            , consumes = MediaType.APPLICATION_JSON_VALUE
            /* 응답 데이터 제한하기 */
            , produces = MediaType.APPLICATION_JSON_VALUE
            /* AUTHORIZATION 헤더값이 admin 일때만 응답을 받겠다는 것 */
            , headers = {HttpHeaders.AUTHORIZATION + "=" + "admin"} )
    public ApiMessageVO helloPost(@Valid @RequestBody SampleVO sampleVO, @RequestParam String name) {
        return ApiMessageVO.builder()
                .resMsg(OK_RESP_MSG)
                .respBody(name + ":" + sampleVO.toString())
                .respCode(OK_RESP_CODE)
                .build();
    }

    /**
     * makingName
     * @param mdn
     * @param prodNo
     * @param sampleVO
     * @return
     * @throws Exception
     */
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

    @Cacheable(value = CacheKey.SAMPLE, key = "#id", unless = "#result == null", cacheManager = "cacheManager")
    @ApiOperation(value = "tooMuchJob", notes = "sample tooMuchJob 함수 입니다.", response = SampleVO.class)
    @GetMapping("/tooMuchJob/{id}")
    public ApiMessageVO tooMuchJob(@PathVariable String id) {
        // todo: 지연테스트용 지워야 함.
        for(long i=0; i<5_000_000_000L; i++) {}
        return ApiMessageVO.builder()
                .resMsg(OK_RESP_MSG)
                .respBody("오래 걸리는 작업 테스트")
                .respCode(OK_RESP_CODE)
                .build();
    }

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


}
