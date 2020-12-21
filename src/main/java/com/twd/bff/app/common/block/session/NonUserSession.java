package com.twd.bff.app.common.block.session;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class NonUserSession implements Serializable {

    private static final long serialVersionUID = 1079510168564042506L;

    private String sessionId;			// session Id
    private String custNm;				// 고객명
    private String svcNumber;   		// 서비스번호
    private String orderId;				// 주문번호
    private String custGrade = "99";	// Tworld 회원등급
    //private String custRgstNum;   	// 주민번호		==> 사용여부 확인 필요
    //private String smsAuthYn;   		// 				==> 사용여부 확인 필요
}
