package com.notification.propel.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TemplateProcessingException extends RuntimeException
            implements IExposableException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final String message;

    public TemplateProcessingException(String message) {
        this.message = message;
    }

}
