package com.twd.bff.app.common.block.exceptions;

import com.twd.bff.app.common.block.exceptions.BaseException;

public class BlockUrlException extends BaseException {

	public BlockUrlException(String exceptionCode) {
		super(exceptionCode);
	}

	/**
	 * @param exceptionCode
	 *            예외 유형을 정의하는 코드.
	 * @param cause
	 *            최초 발생한 예외. 해당 예외는 기본 예외로 전환된다.
	 */
	public BlockUrlException(String exceptionCode, Throwable cause) {
		super(exceptionCode, cause, null);
	}

	/**
	 * @param exceptionCode
	 *            예외 유형을 정의하는 코드.
	 * @param messageArgs
	 *            예외 메시지 변환에 사용되는 메시지 파라미터.
	 */
	public BlockUrlException(String exceptionCode, String... messageArgs) {
		super(exceptionCode, null, messageArgs);
	}

	/**
	 * @param exceptionCode 예외 유형을 정의하는 코드.
	 * @param messageArgs   예외 메시지 변환에 사용되는 메시지 파라미터.
	 */
	public BlockUrlException(String exceptionCode, Object... messageArgs) {
		super(exceptionCode, null, messageArgs);
	}
}
