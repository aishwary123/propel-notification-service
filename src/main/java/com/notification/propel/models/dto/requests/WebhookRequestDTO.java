package com.notification.propel.models.dto.requests;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString
public class WebhookRequestDTO {

    private Template templateDetails;

    @NotNull
    private UUID configId;

    private String message;

    @Data
    @ToString
    @EqualsAndHashCode
    public class Template {

        // If it is set and templateId is not present, default will be used.
        private boolean templateUsed;

        private Map<String, Object> templatePlaceholders = new HashMap<>();
    }
}
