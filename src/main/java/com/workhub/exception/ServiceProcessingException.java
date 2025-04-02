package com.workhub.exception;

import lombok.Data;

@Data
public class ServiceProcessingException extends RuntimeException {

    private static final long serialVersionUID = 1441201016393884947L;

    protected final Integer http;

    protected final Integer code;

    protected final WorkhubException.ErrorType type;

    protected final String message;

    public ServiceProcessingException(
            Integer http, Integer code, WorkhubException.ErrorType type, String message) {
        this.http = http;
        this.code = code;
        this.type = type;
        this.message = message;
    }

    public ServiceProcessingException(
            Integer http,
            Integer code,
            WorkhubException.ErrorType type,
            String message,
            Throwable cause) {
        super(message, cause);
        this.http = http;
        this.code = code;
        this.type = type;
        this.message = message;
    }
}
