package com.notification.propel.exceptions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ValidationException extends RuntimeException
            implements IExposableException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final List<String> messages = new ArrayList<>();

    public ValidationException(String message) {
        this.messages.add(message);
    }

    public ValidationException(List<String> messages) {
        this.messages.addAll(messages);

    }

    @Override
    public String getMessage() {
        return this.messages.stream().collect(Collectors.joining(","));
    }

}
