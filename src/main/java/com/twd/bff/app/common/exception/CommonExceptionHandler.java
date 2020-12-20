package com.twd.bff.app.common.exception;

import com.twd.bff.app.common.vo.ApiMessageVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class CommonExceptionHandler {

    @Autowired
    private ConfigurableEnvironment env;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> unknowError(Exception e) {
        String respBody = "";
        if (getProfile()) {
            respBody = e.getMessage();
        }
        log.error(this.getClass().getName(), e);
        return new ResponseEntity<ApiMessageVO>(ApiMessageVO.builder()
                .respCode("BIZ_001")
                .respBody(respBody)
                .resMsg("일반 오류 입니다.[자세한 내용은 서버에 문의 주세요.]").build(), HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> unknowError(RuntimeException e) {
        String respBody = "";
        if (getProfile()) {
            respBody = e.getMessage();
        }
        log.error(this.getClass().getName(), e);
        return new ResponseEntity<ApiMessageVO>(ApiMessageVO.builder()
                .respCode("BIZ_002")
                .respBody(respBody)
                .resMsg("런타임 오류 입니다.[자세한 내용은 서버에 문의 주세요]").build(), HttpStatus.GATEWAY_TIMEOUT);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> requiredParamError(MissingServletRequestParameterException e) {
        String respBody = "";
        if (getProfile()) {
            respBody = e.getMessage();
        }
        return new ResponseEntity<ApiMessageVO>(ApiMessageVO.builder()
                .respCode("BIZ_003")
                .respBody(respBody)
                .resMsg("필수 파라미터 오류 입니다.[자세한 내용은 서버에 문의 주세요]").build(), HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> invalidParamError(MethodArgumentNotValidException e) {
        StringBuilder respBody = new StringBuilder();
        if (getProfile()) {
            e.getBindingResult().getAllErrors().forEach((error) -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                respBody.append("{" + fieldName + "} " + errorMessage);
            });
        }
        return new ResponseEntity<ApiMessageVO>(ApiMessageVO.builder()
                .respCode("BIZ_004")
                .respBody(respBody.toString())
                .resMsg("파라미터 유효성 오류 입니다.[자세한 내용은 서버에 문의 주세요]").build(), HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<?> validationParamError(BindException e) {
        String respBody = "";
        if (getProfile()) {
            respBody = e.getMessage();
        }
        String firstMsg = e.getBindingResult().getAllErrors().get(0).getCodes()[0];
//        String firstMsg = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return new ResponseEntity<ApiMessageVO>(ApiMessageVO.builder()
                .respCode("BIZ_005")
                .respBody(respBody)
                .resMsg("파라미터 바인딩 오류 입니다.[자세한 내용은 서버에 문의 주세요]: " + firstMsg).build(), HttpStatus.BAD_GATEWAY);
    }

    private boolean getProfile() {
        try {
            String profile = env.getActiveProfiles()[0];
            if (StringUtils.isNotEmpty(profile)) {
                if ("local".equals(profile)) {
                    return true;
                } else if ("default".equals(profile)) {
                    return true;
                } else if ("dev".equals(profile)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception exc) {
            log.error(this.getClass().getName(), exc);
        }
        return false;
    }
}
