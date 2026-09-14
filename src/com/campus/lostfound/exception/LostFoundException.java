package com.campus.lostfound.exception;

/**
 * ⚠️ LostFoundException
 * Demonstrates Unit 3: Base Custom Checked Exception, Exception Hierarchy.
 */
public class LostFoundException extends Exception {
    private final String errorCode;

    public LostFoundException(String message) {
        super(message);
        this.errorCode = "ERR_GENERIC_LOST_FOUND";
    }

    public LostFoundException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public LostFoundException(String message, Throwable cause, String errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String toString() {
        return String.format("[%s]: %s", errorCode, getMessage());
    }
}
