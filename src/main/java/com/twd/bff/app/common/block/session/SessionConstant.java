package com.twd.bff.app.common.block.session;
/**
 * Session 객체용 상수 모음
 */
public final class SessionConstant {

    private SessionConstant() {};

    public static final SESSION_KEY SESSION_KEY_OBJ = new SESSION_KEY() {
    };

    public static class SESSION_KEY {

        /** T아이디 연동 state */
        public static final String STATE = "State";

        /** T아이디 연동 nonce */
        public static final String NONCE = "Nonce";

        /** T아이디 토큰 */
        //public static final String TOKEN = "Token";

        /** 일반회원 정보 */
        public static final String USER = "User";

        /** 비회원 정보*/
        public static final String NON_USER = "NonUser";

        /** 로그인 실패 세션*/
        public static final String LOGIN_FAIL = "LoginFail";
    }

    public static final String YES = "Y";
}

