package com.twd.bff.app.biz.controller;

import com.twd.bff.app.biz.service.SampleService;
import com.twd.bff.app.biz.vo.SampleVO;
import com.twd.bff.app.common.constant.CommonConstant;
import com.twd.bff.app.common.util.masking.MaskingHelper;
import com.twd.bff.app.common.util.masking.MaskingTypeEnum;
import com.twd.bff.app.common.vo.ApiMessageVO;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.twd.bff.app.common.constant.CommonConstant.OK_RESP_CODE;
import static com.twd.bff.app.common.constant.CommonConstant.OK_RESP_MSG;

@Profile(value = {"local", "default"})
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

    @ApiOperation(value = "getNameMaking", notes = "sample getNameMaking 함수 입니다.", response = SampleVO.class)
    @GetMapping("/maskingName")
    public ApiMessageVO getNameMaking(
            @RequestHeader(value="mdn") String mdn,
            @RequestHeader(value="prodNo") String prodNo,
            @ApiParam(value = "SampleVO 파라미터", required = true) SampleVO sampleVO) throws Exception {
        MaskingHelper.init(CommonConstant.LANG_CD);
        String nameType = MaskingTypeEnum.MaskName.getCode();
        String nameMaskingStr = MaskingHelper.maskString(nameType, sampleVO.getName());

        // todo: 지연테스트용 지워야 함.
        for(long i=0; i<5_000_000_000L; i++) {}

        return ApiMessageVO.builder()
                .resMsg(OK_RESP_MSG)
                .respBody(SampleVO.builder().name(nameMaskingStr).build())
                .respCode(OK_RESP_CODE)
                .build();
    }

}
