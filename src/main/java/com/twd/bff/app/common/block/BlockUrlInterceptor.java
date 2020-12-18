package com.twd.bff.app.common.block;

import com.skt.tdirect.core.AppConfig;
import com.skt.tdirect.core.exceptions.BizException;
import com.skt.tdirect.core.mvc.BlockUrlComponent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* <ul>
* <li>업무명 : Block Url Interceptor </li>
* <li>설  명 : 요청을 가로채어 URL 차단 여부를 확인한다.</li>
* <li>작성일 : 2018. 9. 4.</li>
* <li>작성자 : P127602 </li>
* </ul>
*/
@Component
@Slf4j
public class BlockUrlInterceptor extends HandlerInterceptorAdapter {

	/**
	 * appConfig
	 */
	@Autowired
	private AppConfig appConfig;

	/**
	 * blockApi
	 */
	@Autowired
	private BlockUrlComponent blockUrlComponent;

	@Override
	public boolean preHandle( HttpServletRequest request,
								HttpServletResponse response,
								Object handler ) throws Exception {

		try {
			// HTTP 요청 처리 전 수행할 로직 작성
			Cookie cookieBlockHiddenKey = WebUtils.getCookie(request, "block_hidden_key");
			String blockHiddenKey = (cookieBlockHiddenKey ==null) ? "" : cookieBlockHiddenKey.getValue();

			String guideUrl = blockUrlComponent.check(blockHiddenKey, request.getSession(false), request.getRequestURI(), request.getParameterMap());
			if (StringUtils.isNotEmpty(guideUrl)) {
				response.sendRedirect(guideUrl);
				return false;
			}
		} catch (BizException be) {
			throw be;
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		return super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request,
	                       HttpServletResponse response, Object handler,
	                       ModelAndView modelAndView ) throws Exception {
		// HTTP 요청 처리 후 수행할 로직 작성
	}
}
