package com.notification.propel.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RestClientException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final String message;

    public RestClientException(String message) {
        this.message = message;
    }

}
