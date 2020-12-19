package com.twd.bff.app.common.logging;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class LoggingUtil {

    public static SimpleDateFormat format1 = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss.SSS");

    public static String makeLoggingRequestString(final HttpServletRequest request, String logLevel) throws IOException {

        // request info
        StringBuilder requestInfos = new StringBuilder();

        Calendar reqTime = Calendar.getInstance();
        String format_reqTime = format1.format(reqTime.getTime());

        String reqId = "";
        if( request.getParameterMap().containsKey("reqId") ) reqId = request.getParameter("reqId");

        requestInfos.append("|reqId=" + reqId );
        requestInfos.append("|reqInterfaceId=" + request.getServletPath());
        requestInfos.append("|reqTime=" + format_reqTime);

        if("DEBUG".equals(logLevel)){
            setReqHeaderAndBodyToStringBuffer(request, requestInfos);
        }

        return StringUtils.removeStart( requestInfos.toString() , "|");
    }

    private static void setReqHeaderAndBodyToStringBuffer(HttpServletRequest request, StringBuilder requestInfos) throws IOException {

        requestInfos.append("|reqLogUrl=" + request.getRequestURL().toString());
//        requestInfos.append("|reqLogServletPath=" + request.getServletPath());
//        requestInfos.append("|reqLogQueryString=" + request.getQueryString());
        requestInfos.append("|reqLogMethod=" + request.getMethod());
//        requestInfos.append("|reqLogRemoteAddr=" + request.getRemoteAddr());
//        requestInfos.append("|reqLogRemoteHost=" + request.getRemoteHost());
//        requestInfos.append("|reqLogRemotePort=" + request.getRemotePort());
//        requestInfos.append("|reqLogRemoteUser=" + request.getRemoteUser());
        requestInfos.append("|reqLogEncoding=" + request.getCharacterEncoding());

        // request header
        Map<String, Object> requestHeaderMap = new HashMap<>();
        Enumeration<String> requestHeaderNameList = request.getHeaderNames();
        while(requestHeaderNameList.hasMoreElements()) {
            String headerName = requestHeaderNameList.nextElement();
            requestHeaderMap.put(headerName, request.getHeader(headerName));
        }
        requestInfos.append("|reqLogHeader=" + requestHeaderMap);

        // request Body
        try {
            Object requestBody = ((RequestWrapper) request).convertToObject();
            requestInfos.append("|reqLogBody=" + requestBody);
        } catch (IOException iex) {
            throw new IOException(iex);
        }
    }

    public static Map<String, Object> makeLoggingRequestMap(final HttpServletRequest request, String logLevel) throws IOException {
        // request info
        Map<String, Object> requestMap = new HashMap<>();

        Calendar reqTime = Calendar.getInstance();
        String format_reqTime = format1.format(reqTime.getTime());

        String reqId = "";
        if( request.getParameterMap().containsKey("reqId") ) reqId = request.getParameter("reqId");

        requestMap.put("reqId", reqId );
        requestMap.put("reqInterfaceId", request.getServletPath());
        requestMap.put("reqTime", format_reqTime);

        if("DEBUG".equals(logLevel)) {
            setReqHeaderAndBodyToMap(request, requestMap);
        }

        return requestMap;
    }

    private static void setReqHeaderAndBodyToMap(HttpServletRequest request, Map<String, Object> requestMap) throws IOException {

        requestMap.put("reqLogUrl", request.getRequestURL().toString());
//        requestMap.put("reqLogServletPath", request.getServletPath());
//        requestMap.put("reqLogQueryString", request.getQueryString());
        requestMap.put("reqLogMethod", request.getMethod());
//        requestMap.put("reqLogRemoteAddr", request.getRemoteAddr());
//        requestMap.put("reqLogRemoteHost", request.getRemoteHost());
//        requestMap.put("reqLogRemotePort", request.getRemotePort());
//        requestMap.put("reqLogRemoteUser", request.getRemoteUser());
        requestMap.put("reqLogEncoding", request.getCharacterEncoding());

        // request header
        Map<String, Object> requestHeaderMap = new HashMap<>();
        Enumeration<String> requestHeaderNameList = request.getHeaderNames();
        while(requestHeaderNameList.hasMoreElements()) {
            String headerName = requestHeaderNameList.nextElement();
            requestHeaderMap.put(headerName, request.getHeader(headerName));
        }
        requestMap.put("reqLogHeader", requestHeaderMap);

        // request Body
        try {
            Object requestBody = ((RequestWrapper) request).convertToObject();
            if(null == requestBody ){
                //requestBody = request.getParameterMap();
                requestBody = convertMap(request);
            }
            requestMap.put("reqLogBody", requestBody);
        } catch (IOException iex) {
            throw new IOException(iex);
        }
    }

    public static HashMap<String, Object> convertMap(HttpServletRequest request) {

        HashMap<String, Object> hmap = new HashMap<String, Object>();
        String key;

        Enumeration<?> enumObj = request.getParameterNames();

        while (enumObj.hasMoreElements()) {
            key = (String) enumObj.nextElement();
            if (request.getParameterValues(key).length > 1) {
                hmap.put(key, request.getParameterValues(key));
            } else {
                hmap.put(key, request.getParameter(key));
            }

        }

        return hmap;
    }

    public static Map<String, Object> makeLoggingResponseMap(final HttpServletResponse response, String logLevel) throws IOException {
        // response info
        Map<String, Object> responseMap = new HashMap<>();
        if("DEBUG".equals(logLevel)) {
            responseMap.put("respLogStatus", response.getStatus());

            // response header
            Map<String, Object> responseHeaderMap = new HashMap<>();
            Collection<String> responseHeaderNameList = response.getHeaderNames();
            responseHeaderNameList.forEach(v -> responseHeaderMap.put(v, response.getHeader(v)));
            responseMap.put("respLogHeader", responseHeaderMap);
        }

        // response body
        try {
            Calendar respTime = Calendar.getInstance();
            String format_respTime = format1.format(respTime.getTime());
            responseMap.put("respLogTime", format_respTime);

            if( 0 < ((ResponseWrapper) response).getContentSize()) {
                Object responseBody = ((ResponseWrapper) response).convertToObject();

                if("DEBUG".equals(logLevel)) {
                    responseMap.put("respLogBody", responseBody);
                }else{
                    responseMap.put("respLogBody", responseBody.toString().length());
                }
            }

        } catch (IOException ioe) {
            throw new IOException(ioe);
        }

        return responseMap;
    }

    public static String makeLoggingResponseString(final HttpServletResponse response, String logLevel) throws IOException {
        // response info
        StringBuffer responseInfos = new StringBuffer();
        if("DEBUG".equals(logLevel)) {
            responseInfos.append("|respLogStatus="  + response.getStatus());

            // response header
            Map<String, Object> responseHeaderMap = new HashMap<>();
            Collection<String> responseHeaderNameList = response.getHeaderNames();
            responseHeaderNameList.forEach(v -> responseHeaderMap.put(v, response.getHeader(v)));
            responseInfos.append("|respLogHeader=" + responseHeaderMap);
        }

        // response body
        try {
            Object responseBody = ((ResponseWrapper) response).convertToObject();

            if("DEBUG".equals(logLevel)) {

                responseInfos.append("|respLogBody=" + responseBody);
            }else{

                responseInfos.append("|respLogBody=" + responseBody.toString().length());
            }

            Calendar respTime = Calendar.getInstance();
            String format_respTime = format1.format(respTime.getTime());
            responseInfos.append("|respLogTime="+ format_respTime);
        } catch (IOException ioe) {
            throw new IOException(ioe);
        }

        return StringUtils.removeStart( responseInfos.toString() , "|");
    }
}
