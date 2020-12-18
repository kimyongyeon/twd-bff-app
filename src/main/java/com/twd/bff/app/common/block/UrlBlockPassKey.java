package com.twd.bff.app.common.block;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
* <pre> 
* DV-18년-TworldDirect-구조개선
* 1.클래스명 : UrlBlockPassKey.java
* 2.작성일 : 2019. 07. 19.
* 3.작성자 : P028874
* 4.설명 :
* </pre>
*/
@Getter
@Setter
@ToString
@NoArgsConstructor
public class UrlBlockPassKey {

	/** 차단CODE */
	private String blCode;

	/** UR: URL 단독차단 , GR:그룹 차단 , IF:연계시스템 차단 */
	private String blType;

	/** W:온라인,M:모바일 */
	private String blChannel;

	/** 차단URL = 차단 Key*/
	private String blUrl;

	/** 안내화면URL */
	private String guideUrl;

	/** 차단시작시간 */
	private String blStDtm;

	/** 차단종료시간 */
	private String blEdDtm;

	/** 차단 파라미터명 */
	private String paramName;

	/** 차단 파라미터 값 */
	private String paramValue;

	/** 파라미터 차단 여부 */
	private String paramYn;

}
