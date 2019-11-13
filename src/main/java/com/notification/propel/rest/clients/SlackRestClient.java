package com.notification.propel.rest.clients;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.notification.propel.models.db.webhook.WebhookConfig;
import com.notification.propel.models.db.webhook.WebhookConfig.WebhookActionEnum;

@Component
public class SlackRestClient {

    @Autowired
    private RestTemplate restTemplate;

    @Retryable(value = {
                Exception.class }, maxAttempts = 3, backoff = @Backoff(delay = 5000))
    public void sendMessage(final WebhookConfig webhookConfig,
                            final String messageBody) {

        final String slackEndpoint = webhookConfig.getUrl();
        final HttpHeaders httpHeaders = getHeaders(webhookConfig);
        HttpEntity<String> entity = new HttpEntity<>(messageBody, httpHeaders);

        HttpMethod method = HttpMethod.POST;
        if (webhookConfig.getActionType().equals(WebhookActionEnum.PUT)) {
            method = HttpMethod.PUT;
        }
        restTemplate.exchange(slackEndpoint, method, entity, String.class);
    }

    private HttpHeaders getHeaders(final WebhookConfig webhookConfig) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        String contentType = webhookConfig.getContentType();
        httpHeaders.set("content-type", contentType);
        JsonNode node = webhookConfig.getCustomHeaders();
        if (null != node) {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> result = mapper.convertValue(node, Map.class);
            for (String key : result.keySet()) {
                httpHeaders.add(key, String.valueOf(result.get(key)));
            }
        }
        return httpHeaders;
    }
}
