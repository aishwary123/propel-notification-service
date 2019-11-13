package com.notification.propel.models.dto;

/**
 * This data model is used to pass the error related information to client if
 * any error occurs.
 * 
 * @author aishwaryt
 */
public class ErrorResponse {

    private int errorCode;
    private String message;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
