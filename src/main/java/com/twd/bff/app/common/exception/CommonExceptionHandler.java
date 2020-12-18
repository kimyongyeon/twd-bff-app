package com.twd.bff.app.common.exception;

import com.twd.bff.app.common.vo.ApiMessageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> unknowError(Exception e) {
        log.error(this.getClass().getName(), e);
        return new ResponseEntity<ApiMessageVO>(ApiMessageVO.builder()
                .respCode("502")
                .resMsg(e.getClass().getName() + " : " + e.getMessage()).build(), HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> unknowError(RuntimeException e) {
        log.error(this.getClass().getName(), e);
        return new ResponseEntity<ApiMessageVO>(ApiMessageVO.builder()
                .respCode("500")
                .resMsg(e.getClass().getName() + " : " + e.getMessage()).build(), HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> requiredParamError(MissingServletRequestParameterException e) {
        return new ResponseEntity<ApiMessageVO>(ApiMessageVO.builder()
                .respCode("500")
                .resMsg("Required value is missing.").build(), HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> invalidParamError(MethodArgumentNotValidException e) {
        return new ResponseEntity<ApiMessageVO>(ApiMessageVO.builder()
                .respCode("500")
                .resMsg("The request parameter is invalid.").build(), HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<?> validationParamError(BindException e) {
        String firstMsg = e.getBindingResult().getAllErrors().get(0).getCodes()[0];
//        String firstMsg = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return new ResponseEntity<ApiMessageVO>(ApiMessageVO.builder()
                .respCode("500")
                .resMsg("The request parameter is invalid.[" + firstMsg + "]").build(), HttpStatus.BAD_GATEWAY);
    }
}
