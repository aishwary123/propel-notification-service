package com.notification.propel.models.dto.commons;

import lombok.Data;

@Data
public abstract class AuditableDTO<U> {

    protected U createdBy;

    protected Long createdDate;

    protected U lastModifiedBy;

    protected Long lastModifiedDate;
}
