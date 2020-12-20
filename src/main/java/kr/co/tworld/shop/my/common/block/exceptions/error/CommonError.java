package kr.co.tworld.shop.my.common.block.exceptions.error;

import lombok.Getter;

@Getter
public enum CommonError implements Error {
    SYSTEM_ERROR("ERROR.COMMON.5000", "시스템 에러가 발생하였습니다."),
    NOT_FOUND("ERROR.COMMON.4040", "요청하신 페이지를 찾을 수가 없습니다."),
    BLOCKED_URL_ERROR("ERROR.COMMON.5001", "시스템 점검 중입니다."),
    BLOCKED_URL_TIME_ERROR("ERROR.COMMON.5002", "시스템 점검 중입니다.</br>(%s월 %s일 %s시 %s분 ~ %s월 %s일 %s시 %s분)"),
    BLOCKED_URL_TIME_ALL("ERROR.COMMON.ALL", "시스템 점검 중입니다.</br>(%s월 %s일 %s시 %s분 ~ %s월 %s일 %s시 %s분)"),
    BLOCKED_URL_TIME_DSDS("ERROR.COMMON.DSDS", "시스템 점검 중입니다.</br>(%s월 %s일 %s시 %s분 ~ %s월 %s일 %s시 %s분)");

    private String code;
    private String message;

    CommonError(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
