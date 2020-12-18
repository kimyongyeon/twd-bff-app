package com.twd.bff.app.common.util;

import com.twd.bff.app.common.vo.RestBackendApiHeadersVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Component
public class RestBackendAPI {
    @Autowired
    RestTemplate restTemplate;

    public ResponseEntity<?> httpGet(String url, Map<String, Object> Params) {
        final HttpEntity<String> entity = new HttpEntity<String>(setHeader());
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        for (Map.Entry<String, Object> entry : Params.entrySet()) {
            builder.queryParam(entry.getKey(), entry.getValue());
        }
        return restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, Map.class);
    }
    public ResponseEntity<?> httpPost(String url, Map<String, Object> Params) {
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity(Params, setHeader());
        return restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
    }
    public ResponseEntity<?> httpDel(String url, Map Params) {
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity(Params, setHeader());
        return restTemplate.exchange(url, HttpMethod.DELETE, entity, Map.class);
    }
    public ResponseEntity<?> httpPut(String url, Map Params) {
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity(Params, setHeader());
        return restTemplate.exchange(url, HttpMethod.PUT, entity, Map.class);
    }

    public HttpHeaders setHeader() {
        final HttpHeaders headers = new HttpHeaders();
        headers.set(RestBackendApiHeadersVO.key, RestBackendApiHeadersVO.value);
        return headers;
    }


}
