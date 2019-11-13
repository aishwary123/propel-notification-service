package com.notification.propel.models.db.email;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.hibernate.annotations.GenericGenerator;

import com.notification.propel.models.db.commons.Auditable;
import com.notification.propel.models.db.commons.INotificationTemplate;
import com.notification.propel.models.dto.enums.TemplateEngineType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class EmailTemplate extends Auditable<String>
            implements INotificationTemplate, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private String templateName;

    private String templateDescription;

    @Lob
    private String templateBody;

    private TemplateEngineType templateEngineType;

    private boolean isDefault;
}
