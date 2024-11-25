package com.workhub.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WorkhubException {

    BAD_REQUEST(400, 400, ErrorType.ERROR_IN_FORMAT, "Format not valid"),
    UNAUTHORIZED(401, 401, ErrorType.NOT_AUTHORIZED, "Not authenticated"),
    NOT_FOUND(404, 404, ErrorType.NOT_FOUND, "Resource not Found"),
    INTERNAL_SERVER_ERROR(500, 500, ErrorType.INTERNAL_SERVER_ERROR, "Something went wrong on the server side");

    private final Integer http;

    private final Integer code;

    private final ErrorType type;

    private final String message;

    public enum ErrorType {
        ERROR_IN_FORMAT,
        NOT_AUTHORIZED,
        NOT_FOUND,
        INTERNAL_SERVER_ERROR
    }
}
