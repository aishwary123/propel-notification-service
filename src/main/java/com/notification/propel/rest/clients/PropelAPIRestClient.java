package com.notification.propel.rest.clients;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.notification.propel.exceptions.RestClientException;
import com.notification.propel.messages.CustomMessages;
import com.notification.propel.utils.SystemUtils;

import lombok.Data;

/**
 * Propel is a service which gives all the shards/tenants related information in
 * any specific landscape.
 * 
 * @author aishwaryt
 */

@Component
public class PropelAPIRestClient {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${PROPEL_API_ENDPOINT_EXTERNAL}")
    private String externalPropelAPIEndpoint;

    private static final String PROPEL_API_ENDPOINT = "PROPEL_API_ENDPOINT";

    @Autowired
    private RestTemplate restTemplate;

    private String propelAPIEndpoint;
    private static final String SHARD_ID_ENDPOINT_TEMPLATE = "/orgShards/%s";
    private static final String ALL_SHARD_ID_ENDPOINT = "/orgShards";

    @PostConstruct
    public void prepareEndpoints() {

        this.propelAPIEndpoint = SystemUtils.getSystemVariableOrDefault(
                    PROPEL_API_ENDPOINT, externalPropelAPIEndpoint);

    }

    public String getShardIdFromOrgId(final String orgId) {

        try {

            String endpoint = String.format(SHARD_ID_ENDPOINT_TEMPLATE, orgId);
            endpoint = propelAPIEndpoint.concat(endpoint);
            String response = restTemplate.getForObject(endpoint, String.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonObject = mapper.readTree(response);
            JsonNode shardId = jsonObject.get("shardId");
            return "shard".concat(String.valueOf(shardId.intValue()));

        } catch (IOException ioException) {
            logger.error(
                        CustomMessages.FETCHING_TENAND_ID_FROM_ORG_ID_FAILED.concat(
                                    CustomMessages.PLACEHOLDER),
                        ioException.getMessage());
            throw new RestClientException(
                        CustomMessages.FETCHING_TENAND_ID_FROM_ORG_ID_FAILED);
        }

    }

    public List<String> getAllShardId() {

        try {

            String endpoint = String.format(ALL_SHARD_ID_ENDPOINT);
            endpoint = propelAPIEndpoint.concat(endpoint);
            String response = restTemplate.getForObject(endpoint, String.class);
            ObjectMapper mapper = new ObjectMapper();
            List<ShardDetail> shardDetails = Arrays.asList(
                        mapper.readValue(response, ShardDetail[].class));
            return shardDetails.parallelStream().map(
                        elem -> "shard".concat(elem.getShardId())).collect(
                                    Collectors.toList());

        } catch (IOException ioException) {
            logger.error(
                        CustomMessages.FETCHING_SHARD_DETAILS_FAILED.concat(
                                    CustomMessages.PLACEHOLDER),
                        ioException.getMessage());
            throw new RestClientException(
                        CustomMessages.FETCHING_SHARD_DETAILS_FAILED);
        }

    }

    @Data
    public static class ShardDetail {
        private String shardId;
        private String orgId;
        private String createdAt;
    }
}
