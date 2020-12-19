package com.twd.bff.app.biz.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SampleVO {
    @ApiModelProperty(position = 1, value = "이름", example = "홍길동", required = true)
    String name;
    @ApiModelProperty(position = 2, value = "전화번호", example = "010-1234-1234", required = false)
    String tel;
    @ApiModelProperty(position = 3, value = "주소", example = "서울특별시 종로구 패럼타워 14층", required = false)
    String address;
    @ApiModelProperty(position = 4, value = "이메일", example = "twd_admin@sk.com", required = false)
    String email;
}
