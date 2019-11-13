package com.notification.propel.models.dto.webhook;

import com.fasterxml.jackson.databind.JsonNode;
import com.notification.propel.models.dto.commons.AuditableDTO;
import com.notification.propel.models.dto.commons.INotificationConfigDTO;
import com.notification.propel.models.dto.enums.TemplateEngineType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WebhookConfigDTO extends AuditableDTO<String>
            implements INotificationConfigDTO {

    public enum WebhookActionEnum {
        POST, PUT
    }

    private String url;

    private String contentType;

    private String payload;

    private WebhookActionEnum actionType;

    private JsonNode customHeaders;

    private boolean isDefault;

    private TemplateEngineType templateEngineType;
}
