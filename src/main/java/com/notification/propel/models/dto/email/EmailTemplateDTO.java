package com.notification.propel.models.dto.email;

import javax.validation.constraints.NotNull;

import com.notification.propel.models.dto.commons.AuditableDTO;
import com.notification.propel.models.dto.commons.INotificationTemplateDTO;
import com.notification.propel.models.dto.enums.TemplateEngineType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EmailTemplateDTO extends AuditableDTO<String>
            implements INotificationTemplateDTO {

    @NotNull
    private String templateName;

    @NotNull
    private String templateDescription;

    @NotNull
    private String templateBody;

    @NotNull
    private TemplateEngineType templateEngineType;

    private boolean isDefault;

}
