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
public class UserSession implements Serializable {

    private static final long serialVersionUID = 406614525163228342L;

    private String loginId; 		// Tworld 고객ID
    private String custId;  		// Tshop 고객번호
    private String custNm;  		// 고객명
    private String custGrade;   	// Tworld 회원등급(A,Y,R,D,P,M,N,O)
    private String svcNumber;   	// 서비스번호
    private String svcMgmtNum;  	// 서비스관리번호
    private String eqpMdlCd;		// 현재 사용중인 단말기 코드
    private String eqpMdlNm;		// 현재 사용중인 단말기 이름
    private String chnlMbrId;		// 채널회원아이디(통합ID)
    private String svcCd;   		// 서비스구분코드
    private String underAgeFlag;   	// 미성년여부(Y/N)
    private String custTypCd;   	// 고객유형코드
    private String sexCd;			// 성별코드 1/2
    private String email;			// 고객이메일
    private String birthDt;			// 생년월일
    private String userGbn;			// 내외국인구분(내국인:1 / 외국인:2)
    private String icasProdId;		// ICAS 요금상품ID
    private int age;  				// 고객나이
    private String dupScrbInfo;     // 고객의 DI // DSDS 나의 쇼핑 내역 추가를 위해 추가
}

