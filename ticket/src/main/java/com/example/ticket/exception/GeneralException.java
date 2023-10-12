package com.example.ticket.exception;

import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException{
    private final Code errCode;

    public GeneralException() {
        super(Code.INTERNAL_ERROR.getMessage());
        this.errCode = Code.INTERNAL_ERROR;
    }
    public GeneralException(String message) {
        super(Code.INTERNAL_ERROR.getMessage(message));
        this.errCode = Code.INTERNAL_ERROR;
    }

    public GeneralException(String message, Throwable cause) {
        super(Code.INTERNAL_ERROR.getMessage(message), cause);
        this.errCode = Code.INTERNAL_ERROR;
    }

    public GeneralException(Throwable cause) {
        super(Code.INTERNAL_ERROR.getMessage(cause));
        this.errCode = Code.INTERNAL_ERROR;
    }

    public GeneralException(Code errorCode) {
        super(errorCode.getMessage());
        this.errCode = errorCode;
    }

    public GeneralException(Code errorCode, String message) {
        super(errorCode.getMessage(message));
        this.errCode = errorCode;
    }

    public GeneralException(Code errorCode, String message, Throwable cause) {
        super(errorCode.getMessage(message), cause);
        this.errCode = errorCode;
    }

    public GeneralException(Code errorCode, Throwable cause) {
        super(errorCode.getMessage(cause), cause);
        this.errCode = errorCode;
    }
}
