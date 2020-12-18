package com.twd.bff.app.common.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiMessageVO<T> {
    private String respCode;
    private String resMsg;
    private T respBody;
}

