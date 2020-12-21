package com.twd.bff.app.common.block.exceptions;

import java.util.Arrays;

public class BaseException extends RuntimeException {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -755051317406181157L;

    /**
     * 발생한 예외의 유형을 정의하는 코드. 유형 별 Exception 클래스를 추가하지 않고, 코드로 구분 할 수 있다.
     */
    private String exceptionCode;

    /**
     * 예외 메시지 변환에 사용되는 메시지 파라미터를 설정하는 필드.
     */
    private String[] messageArgs;

    private String debugMessage;

    /**
     * @param exceptionCode
     *            예외 유형을 정의하는 코드.
     */
    public BaseException(String exceptionCode) {
        this(exceptionCode, null, null);
    }

    /**
     * @param exceptionCode
     *            예외 유형을 정의하는 코드.
     * @param cause
     *            최초 발생한 예외. 해당 예외는 기본 예외로 전환된다.
     */
    public BaseException(String exceptionCode, Throwable cause) {
        this(exceptionCode, cause, null);

    }

    /**
     * @param exceptionCode
     *            예외 유형을 정의하는 코드.
     * @param messageArgs
     *            예외 메시지 변환에 사용되는 메시지 파라미터.
     */
    public BaseException(String exceptionCode, String... messageArgs) {
        this(exceptionCode, null, messageArgs);
    }

    public BaseException(String exceptionCode, Throwable cause, String... messageArgs) {
        super(cause!=null?cause.getMessage():exceptionCode, cause);

        if (exceptionCode == null)
            throw new IllegalArgumentException("Exception Code can not be 	null.");

        this.exceptionCode = exceptionCode;
        if (null != messageArgs) {
            this.messageArgs = Arrays.copyOf(messageArgs, messageArgs.length, String[].class);
        }
    }

    /**
     * @param exceptionCode 예외 유형을 정의하는 코드.
     * @param messageArgs   예외 메시지 변환에 사용되는 메시지 파라미터.
     */
    public BaseException(String exceptionCode, Object... messageArgs) {
        this(exceptionCode, null, messageArgs);
    }

    public BaseException(String exceptionCode, Throwable cause, Object... messageArgs) {
        super(cause != null ? cause.getMessage() : exceptionCode, cause);
        if (exceptionCode == null)
            throw new IllegalArgumentException("Exception Code can not be  null.");
        this.exceptionCode = exceptionCode;
        if (null != messageArgs) {
            this.messageArgs = Arrays.copyOf(messageArgs, messageArgs.length, String[].class);
        }
    }


    public void setExceptionCode(String exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    public void setMessageArgs(String[] messageArgs) {
        this.messageArgs = messageArgs;
    }

    public String getDebugMessage() {
        return debugMessage;
    }

    public void setDebugMessage(String debugMessage) {
        this.debugMessage = debugMessage;
    }

    /**
     * @return 예외 유형을 정의하는 예외 코드
     */
    public String getExceptionCode() {
        return exceptionCode;
    }

    /**
     * @return 예외 메시지 변환에 사용되는 메시지 파라미터
     */
    public String[] getMessageArgs() {
        return this.messageArgs;
    }
}
