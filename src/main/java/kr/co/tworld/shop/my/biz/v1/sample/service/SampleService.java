package kr.co.tworld.shop.my.biz.v1.sample.service;

import kr.co.tworld.shop.my.biz.v1.sample.feign.SampleFeign;
import kr.co.tworld.shop.my.common.util.RestBackendAPI;
import kr.co.tworld.shop.my.common.vo.RestBackendApiHeadersVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;


public interface SampleService {

    String get(Map param);
    String post(Map param);
    String del(Map param);
    String put(Map param);

    String hello(String name);

    @Service
    @Slf4j
    class SampleServiceImpl implements SampleService {

        @Autowired
        RestBackendAPI restBackendAPI;

        @Autowired
        SampleFeign sampleFeign;

        public String hello(String name) {
            return sampleFeign.hello(name);
        }

        @Override
        public String get(Map param) {
            RestBackendApiHeadersVO.url = "http://localhost:10011/api/v1/sample/hello";
            RestBackendApiHeadersVO.key = "test";
            RestBackendApiHeadersVO.value = "test12345";
            ResponseEntity<?> responseEntity = restBackendAPI.httpGet(RestBackendApiHeadersVO.url, param);
            log.debug("respBody: " + responseEntity.getBody());
            Map resultMap = (Map) responseEntity.getBody();
            return resultMap.get("respBody").toString();
        }

        @Override
        public String post(Map param) {
            RestBackendApiHeadersVO.url = "http://localhost:10011/api/v1/sample/helloPost";
            HttpEntity<?> httpEntity = restBackendAPI.httpPost(RestBackendApiHeadersVO.url, param);
            return httpEntity.toString();
        }

        @Override
        public String del(Map param) {
            RestBackendApiHeadersVO.url = "http://localhost:10011/api/v1/sample/helloPost";
            HttpEntity<?> httpEntity = restBackendAPI.httpDel(RestBackendApiHeadersVO.url, param);
            return httpEntity.getBody().toString();
        }

        @Override
        public String put(Map param) {
            RestBackendApiHeadersVO.url = "http://localhost:10011/api/v1/sample/helloPost";
            HttpEntity<?> httpEntity = restBackendAPI.httpPut(RestBackendApiHeadersVO.url, param);
            return httpEntity.toString();
        }
    }
}

