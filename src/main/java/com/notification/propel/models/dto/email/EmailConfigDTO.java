package com.notification.propel.models.dto.email;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.notification.propel.models.dto.commons.AuditableDTO;
import com.notification.propel.models.dto.commons.INotificationConfigDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EmailConfigDTO extends AuditableDTO<String>
            implements INotificationConfigDTO {

    public enum EmailEncryptionMethod {
        SSL, TLS
    }

    @NotNull
    private String host;

    @Min(0)
    @Max(65535)
    private int port;

    @NotNull
    private String username;

    @NotNull
    private String password;

    private boolean isAuthRequired;

    private EmailEncryptionMethod emailEncryptionMethod;

    private boolean isDefault;

    private int connectionTimeout = 5000;

    private int timeout = 5000;

    private int writeTimeout = 5000;

}
