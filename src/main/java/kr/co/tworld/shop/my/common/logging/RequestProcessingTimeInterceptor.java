package kr.co.tworld.shop.my.common.logging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Profile(value = {"local", "default"})
@Slf4j
@Component
public class RequestProcessingTimeInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("Request Url: " + request.getRequestURL().toString());
        log.info("Request QueryString: " + request.getQueryString());

        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
        return HandlerInterceptor.super.preHandle(request, response, handler);

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        long midTime = System.currentTimeMillis();
        // 정적 리소드들(css, js,...)은 modelAndView가 null

        // todo: 지연테스트용 지워야 함.
//        for(long i=0; i<10_000_000_000L; i++) {}

        if (modelAndView != null) {
            // modelAndView를 통해 view에 전달할 자료의 수정 가능
            // modelAndView.addObject("message", "Hello Interceptor");
        }
        request.setAttribute("midTime", midTime);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        long endTime = System.currentTimeMillis();
        Object midTimeObj = request.getAttribute("midTime");
        Object startTimeObj = request.getAttribute("startTime");
        log.info("Servlet: {}", request.getServletPath());
        if (midTimeObj != null) {
            long handlerSecond = TimeUnit.MILLISECONDS.toSeconds(endTime - (Long) midTimeObj);
            log.info("Handler Second: {}sec", handlerSecond);
        }
        long seconds = TimeUnit.MILLISECONDS.toSeconds(endTime - (Long) startTimeObj);
        log.info("Total Second: {}sec", seconds);
    }

}
