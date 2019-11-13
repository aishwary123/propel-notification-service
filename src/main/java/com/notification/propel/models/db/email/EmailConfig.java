package com.notification.propel.models.db.email;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import com.notification.propel.models.db.commons.Auditable;
import com.notification.propel.models.db.commons.INotificationConfig;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class EmailConfig extends Auditable<String>
            implements INotificationConfig, Serializable {

    public enum EmailEncryptionMethod {
        SSL, TLS
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

    private String host;

    private int port;

    private String username;

    private String password;

    private boolean isAuthRequired;

    @Enumerated(EnumType.STRING)
    private EmailEncryptionMethod emailEncryptionMethod;

    private boolean isDefault;

    private int connectionTimeout;

    private int timeout;

    private int writeTimeout;

}
