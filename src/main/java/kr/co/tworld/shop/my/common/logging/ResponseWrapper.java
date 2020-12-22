package kr.co.tworld.shop.my.common.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseWrapper extends ContentCachingResponseWrapper {
    private ObjectMapper objectMapper;

    public ResponseWrapper(HttpServletResponse response) {
        super(response);
        this.objectMapper = new ObjectMapper();
    }

    public Object convertToObject() throws IOException {
        return objectMapper.readValue(getContentAsByteArray(), Object.class);
    }
}

