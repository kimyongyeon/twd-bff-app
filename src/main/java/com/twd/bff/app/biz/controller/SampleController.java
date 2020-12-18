package com.twd.bff.app.biz.controller;

import com.twd.bff.app.biz.service.SampleService;
import com.twd.bff.app.biz.vo.SampleVO;
import com.twd.bff.app.common.vo.ApiMessageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.twd.bff.app.common.constant.CommonConstant.OK_RESP_CODE;
import static com.twd.bff.app.common.constant.CommonConstant.OK_RESP_MSG;

@RequestMapping("/api/v1/sample")
@RestController
@Api(value = "SampleController V1")
public class SampleController {

    @Autowired
    SampleService sampleService;

    @ApiOperation(value = "get", notes = "sample get 함수 입니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK !!"),
            @ApiResponse(code = 500, message = "Internal Server Error !!"),
            @ApiResponse(code = 404, message = "Not Found !!")
    })
    @GetMapping("/get")
    public ApiMessageVO get() {
        return ApiMessageVO.builder()
                .resMsg(OK_RESP_MSG)
                .respBody(sampleService.get())
                .respCode(OK_RESP_CODE)
                .build();
    }

    @ApiOperation(value = "hello", notes = "sample hello 함수 입니다.")
    @GetMapping("/hello")
    public ApiMessageVO hello() {
        return ApiMessageVO.builder()
                .resMsg(OK_RESP_MSG)
                .respBody("hello")
                .respCode(OK_RESP_CODE)
                .build();
    }

}
