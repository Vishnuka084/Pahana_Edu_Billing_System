package com.pahanaedu.billingsystem.Exception;

/**
 * Author: Vishnuka Yahan De Silva
 * User:macbookair
 * Date:2025-08-16
 * Time:11:40
 */
public class ConstrainViolationException extends RuntimeException{
    public ConstrainViolationException() {
    }
    public ConstrainViolationException(String message) {
        super(message);
    }

    public ConstrainViolationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConstrainViolationException(Throwable cause) {
        super(cause);
    }

    public ConstrainViolationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
