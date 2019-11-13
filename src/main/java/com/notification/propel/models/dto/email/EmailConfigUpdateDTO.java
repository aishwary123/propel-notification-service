package com.notification.propel.models.dto.email;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EmailConfigUpdateDTO extends EmailConfigDTO {

    @NotNull
    @com.notification.propel.validators.UUID
    private UUID id;

}
