package com.notification.propel.models.db.webhook;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.fasterxml.jackson.databind.JsonNode;
import com.notification.propel.models.db.commons.Auditable;
import com.notification.propel.models.db.commons.INotificationConfig;
import com.notification.propel.models.dto.enums.TemplateEngineType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class WebhookConfig extends Auditable<String>
            implements INotificationConfig, Serializable {

    public enum WebhookActionEnum {
        POST, PUT
    }

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private String url;

    private String contentType;

    private String payload;

    @Enumerated(EnumType.STRING)
    private WebhookActionEnum actionType;

    @Column(columnDefinition = "jsonb")
    @Type(type = "jsonb")
    private JsonNode customHeaders;

    private boolean isDefault;

    private TemplateEngineType templateEngineType;
}
