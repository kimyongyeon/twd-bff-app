package kr.co.tworld.shop.my.config;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Value("${restClient.readTime:0}")
    private int READ_TIMEOUT;

    @Value("${restClient.connTimeout:0}")
    private int CONN_TIMEOUT;

    @Value("${restClient.connectionMaxTotal:20}")
    private int CONN_MAX_TOTAL;

    @Value("${restClient.defaultMaxPerRoute:20}")
    private int CONN_DEFAULT_MAX_PER_ROUTE;

    @Value("${restClient.maxPerRoute:50}")
    private int CONN_MAX_PER_ROUTE;

    @Value("${restClient.url:localhost}")
    private String url = "localhost";

    @Bean(name = "restTemplate")
    public RestTemplate restTemplate() {
        RestTemplate rt = new RestTemplate(getHttpRequestFactory());
        return rt;
    }

    private HttpComponentsClientHttpRequestFactory getHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(CONN_MAX_TOTAL); // 최대 커넥션 수: 서버자원현황, 요청수를 계산해서 설정해야 함.
        cm.setDefaultMaxPerRoute(CONN_DEFAULT_MAX_PER_ROUTE); // 예를 들어 http://localhost:8080/testA, http://localhost:8080/testB 두개의 경로에 대해서 별도로 설정이 없다면 최대 20개의 연결이 생성된다는 뜻이다.
        HttpHost localhost = new HttpHost(url);
        cm.setMaxPerRoute(new HttpRoute(localhost), CONN_MAX_PER_ROUTE); // 최대 연결 개수

        // 타임아웃 설정
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(CONN_TIMEOUT)
                .setConnectionRequestTimeout(CONN_TIMEOUT)
                .setSocketTimeout(READ_TIMEOUT).build();

        CloseableHttpClient client = HttpClients.custom().setConnectionManager(cm).setDefaultRequestConfig(config).build();
        factory.setHttpClient(client);

        return factory;
    }

}


