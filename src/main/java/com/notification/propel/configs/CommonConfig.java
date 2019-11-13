package com.notification.propel.configs;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.notification.propel.utils.SystemUtils;

/**
 * All the beans which are common across this application are defined here.
 * 
 * @author aishwaryt
 */
@Configuration
public class CommonConfig {

    private static final String REST_TEMPLATE_CONNECTION_TIMEOUT = "REST_TEMPLATE_CONNECTION_TIMEOUT";
    private static final String REST_TEMPLATE_READ_TIMEOUT = "REST_TEMPLATE_READ_TIMEOUT";

    @Value("${REST_TEMPLATE_CONNECTION_TIMEOUT}")
    private String defaultRestTemplateConnectionTimeout;

    @Value("${REST_TEMPLATE_READ_TIMEOUT}")
    private String defaultRestTemplateReadTimeout;

    @Bean
    public RestTemplate restTemplate() {

        final int connectionTimeoutInMillis = Integer.parseInt(
                    SystemUtils.getSystemVariableOrDefault(
                                REST_TEMPLATE_CONNECTION_TIMEOUT,
                                defaultRestTemplateConnectionTimeout));

        final int readTimeoutInMillis = Integer.parseInt(
                    SystemUtils.getSystemVariableOrDefault(
                                REST_TEMPLATE_READ_TIMEOUT,
                                defaultRestTemplateReadTimeout));
        CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(
                    new NoopHostnameVerifier()).build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectionRequestTimeout(connectionTimeoutInMillis);
        requestFactory.setReadTimeout(readTimeoutInMillis);
        requestFactory.setHttpClient(httpClient);
        return new RestTemplate(requestFactory);

    }

}
