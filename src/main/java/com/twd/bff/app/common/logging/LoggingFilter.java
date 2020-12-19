package com.twd.bff.app.common.logging;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class LoggingFilter extends OncePerRequestFilter {

    protected String targetActive = "";
    private String ACTIVE_PROFILE = "prd";

    public LoggingFilter(String active) {
        this.targetActive = active;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException {

        Map logMap = new HashMap();
        log.debug("LoggingFilter::this.targetActive::::" + this.targetActive);

        try {
            StopWatch sw = new StopWatch();
            sw.start();

            final HttpServletRequest request = new RequestWrapper(req);
            final HttpServletResponse response = new ResponseWrapper(res);

            String requestString = "";
            Map<String, Object> requestMap = null;

            if(ACTIVE_PROFILE.equals(this.targetActive)){
                requestString = LoggingUtil.makeLoggingRequestString(request, "INFO");
            }else{
                requestMap = LoggingUtil.makeLoggingRequestMap(request, "DEBUG");
            }
            filterChain.doFilter(request, response);
            String responseString = "";
            Map<String, Object> responseMap = null;
            logMap.put("CONTENT-TYPE", request.getContentType() );
            if (request.getContentType() != null && request.getContentType().contains("application/json")) {

                if(ACTIVE_PROFILE.equals(this.targetActive)){
                    responseString = LoggingUtil.makeLoggingResponseString(response, "INFO");
                }else{
                    responseMap = LoggingUtil.makeLoggingResponseMap(response, "DEBUG");
                }
            }
            sw.stop();
            Long total = sw.getTotalTimeMillis();
            if(ACTIVE_PROFILE.equals(this.targetActive)){
                logMap.put("TIME", total);
                logMap.put("REQ", requestString);
                logMap.put("RESP", responseString);
                log.info("active_profile::" + this.targetActive + " => " + new Gson().toJson(logMap));
            }else{
                logMap.put("TIME", total);
                logMap.put("REQ", requestMap);
                logMap.put("RESP", responseMap);
                log.info("active_profile::" + this.targetActive + " => " + new Gson().toJson(logMap));
            }
            ((ResponseWrapper) response).copyBodyToResponse();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

