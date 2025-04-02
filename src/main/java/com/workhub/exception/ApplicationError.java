package com.workhub.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationError {

    private int code;

    private String type;

    private String message;

    public ApplicationError(int code, String type, String message) {
        this.code = code;
        this.type = type;
        this.message = message;
    }

    public ApplicationError() {}
}
