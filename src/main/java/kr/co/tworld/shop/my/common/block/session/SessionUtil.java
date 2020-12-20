package kr.co.tworld.shop.my.common.block.session;

import java.util.Arrays;
import javax.servlet.http.HttpSession;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SessionUtil {

    private SessionUtil() {
    }

    /** TID 인증용 state/nonce Session 생성 */
    public static void setTidSession(String state, String nonce, HttpSession session) {
        session.setAttribute(SessionConstant.SESSION_KEY.STATE, state);
        session.setAttribute(SessionConstant.SESSION_KEY.NONCE, nonce);
    }

    /** TID 인증용 state/nonce/returnURL Session 삭제 */
    public static void removeTidSession(HttpSession session) {
        session.removeAttribute(SessionConstant.SESSION_KEY.STATE);
        session.removeAttribute(SessionConstant.SESSION_KEY.NONCE);
    }

    /** 로그인 실패 Session 생성 */
    public static void setLoginFailSession(HttpSession session) {
        session.setAttribute(SessionConstant.SESSION_KEY.LOGIN_FAIL, SessionConstant.YES);
    }

    /** 로그인 실패 Session 삭제 */
    public static void removeLoginFailSession(HttpSession session) {
        session.removeAttribute(SessionConstant.SESSION_KEY.LOGIN_FAIL);
    }

    /** 일반 로그인 후 Session 생성 */
    public static void setUserSession(UserSession userSession, HttpSession session) {
        session.setAttribute(SessionConstant.SESSION_KEY.USER, userSession);
    }

    /** 비회원 로그인 후 Session 생성 */
    public static void setNonUserSession(NonUserSession nonUserSession, HttpSession session) {
        nonUserSession.setSessionId(session.getId());
        session.setAttribute(SessionConstant.SESSION_KEY.NON_USER, nonUserSession);
    }

    /** 비회원 Session 삭제 */
    public static void removeNonUserSession(HttpSession session) {
        session.removeAttribute(SessionConstant.SESSION_KEY.NON_USER);
    }

    /** 로그아웃 시 Session에 저장된 정보 삭제 */
    public static void removeSessionAttribute(HttpSession session) {

        Arrays.asList(SessionConstant.SESSION_KEY_OBJ.getClass().getFields()).forEach(field -> {
            try {
                session.removeAttribute(String.valueOf(field.get(SessionConstant.SESSION_KEY_OBJ)));
            } catch (Exception e) {
                log.warn("ignore");
            }
        });

        session.invalidate();

        log.info("removeSessionAttribute()");
    }

    /** 회원 혹은 비회원 로그인 여부 확인 */
    public static boolean isLogin(HttpSession session) {
        return isUserLogin(session)|| isNonUserLogin(session);
    }

    /** 회원 로그인 여부 확인 */
    public static boolean isUserLogin(HttpSession session) {
        return session.getAttribute(SessionConstant.SESSION_KEY.USER) != null;
    }

    /** 비회원 로그인 여부 확인 */
    public static boolean isNonUserLogin(HttpSession session) {
        return session.getAttribute(SessionConstant.SESSION_KEY.NON_USER) != null;
    }

    /**
     * TID state 리턴
     **/
    public static String getTidState(HttpSession session) {
        return (String) session.getAttribute(SessionConstant.SESSION_KEY.STATE);
    }

    /**
     * TID nonce 리턴
     **/
    public static String getTidNonce(HttpSession session) {
        return (String) session.getAttribute(SessionConstant.SESSION_KEY.NONCE);
    }

    /**
     * 사용자 세션 정보
     * - 일반 로그인인 경우 세션 정보 제공
     * */
    public static UserSession getUser(HttpSession session) {
        if(isLogin(session)) {
            return (UserSession) session.getAttribute(SessionConstant.SESSION_KEY.USER);
        }else {
            return null;
        }
    }

    /** 비회원 사용자 세션 정보
     * - 비회원 로그인인 경우 세션 정보 제공
     */
    public static NonUserSession getNonUser(HttpSession session) {
        if(isNonUserLogin(session)) {
            return (NonUserSession) session.getAttribute(SessionConstant.SESSION_KEY.NON_USER);
        }else {
            return null;
        }
    }

    /**
     * 로그인 실패 여부 조회
     *   - TID 로그인 성공 & 다이렉트 로그인 실패시 화면 무한 루프 방지를 위해.
     *   - 화면에서는 SessionUtil.getLoginFail()이 false인 경우에만 SSO 로그인 수행해야 함.
     **/
    public static boolean isLoginFail(HttpSession session) {
        String loginFail = (String) session.getAttribute(SessionConstant.SESSION_KEY.LOGIN_FAIL);

        if( loginFail == null ) {
            return false;

        }else {
            if(SessionConstant.YES.equals(loginFail)) {
                return true;
            }else {
                return false;
            }
        }
    }
}
