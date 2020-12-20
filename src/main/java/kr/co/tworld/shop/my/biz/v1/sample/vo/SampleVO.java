package kr.co.tworld.shop.my.biz.v1.sample.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SampleVO {

    @ApiModelProperty(position = 1, value = "이름", example = "홍길동", required = true)
    @NotBlank(message = "이름를 작성 해주세요.")
    String name;

    @ApiModelProperty(position = 2, value = "전화번호", example = "01012341234", required = false)
    @NotBlank(message = "전화번호를 작성 해주세요.")
    @Pattern(regexp = "[0-9]{10,11}", message = "10~11자리의 숫자만 입력가능합니다")
    String tel;

    @ApiModelProperty(position = 3, value = "주소", example = "서울특별시 종로구 패럼타워 14층", required = false)
    String address;

    @ApiModelProperty(position = 4, value = "이메일", example = "twd_admin@sk.com", required = false)
    @NotBlank(message = "메일을 작성 해주세요.")
    @Email(message = "메일의 양식을 지켜주세요.")
    String email;
}
