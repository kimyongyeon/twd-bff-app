package com.twd.bff.app.biz.service;

import com.twd.bff.app.common.util.RestBackendAPI;
import com.twd.bff.app.common.vo.ApiMessageVO;
import com.twd.bff.app.common.vo.RestBackendApiHeadersVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


public interface SampleService {

    String get();
    String post();
    String del();
    String put();

    @Service
    class SampleServiceImpl implements SampleService {

        @Autowired
        RestBackendAPI restBackendAPI;

        @Override
        public String get() {
            RestBackendApiHeadersVO.key = "test";
            RestBackendApiHeadersVO.value = "test12345";
            ResponseEntity<?> responseEntity = restBackendAPI.httpGet("/api/v1/sample/hello", new HashMap<>());
            return responseEntity.getBody().toString();
        }

        @Override
        public String post() {
            restBackendAPI.httpPost("/api/v1/sample/hello", new HashMap<>());
            return "ok";
        }

        @Override
        public String del() {
            restBackendAPI.httpDel("/api/v1/sample/hello", new HashMap<>());
            return "ok";
        }

        @Override
        public String put() {
            restBackendAPI.httpPut("/api/v1/sample/hello", new HashMap<>());
            return "ok";
        }
    }
}

