package com.twd.bff.app.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@ApiModel
@Data
@Builder
public class ApiMessageVO<T> implements Serializable {
    @ApiModelProperty(position = 1, value = "응답코드", example = "200", required = true)
    private String respCode;
    @ApiModelProperty(position = 2, value = "응답메시지", example = "성공 입니다.", required = true)
    private String resMsg;
    @ApiModelProperty(position = 3, value = "응답바디", example = "바디 데이터 입니다.", required = true)
    private T respBody;
}

