package com.twd.bff.app.common.block.exceptions;

import com.twd.bff.app.common.block.exceptions.error.Error;
import lombok.Getter;

@Getter
public class BizException extends BaseException {

    private String code;
    private String message;

    public BizException(Error error) {
        super(error.getCode(), error.getMessage());

        this.code = error.getCode();
        this.message = error.getMessage();
    }

    public BizException(Error error, Object... args) {
        super(error.getCode(), String.format(error.getMessage(), args));

        this.code = error.getCode();
        this.message = String.format(error.getMessage(), args);
    }

    public BizException(String code, String message) {
        super(code, message);

        this.code = code;
        this.message = message;
    }

    public BizException(String code, String message, Object... args) {
        super(code, String.format(message, args));

        this.code = code;
        this.message = message;
    }
}

