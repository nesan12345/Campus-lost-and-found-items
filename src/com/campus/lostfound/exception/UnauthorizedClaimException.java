package com.campus.lostfound.exception;

/**
 * 🚫 UnauthorizedClaimException
 * Demonstrates Unit 3: Exception thrown on unauthorized or mismatched claim attempts.
 */
public class UnauthorizedClaimException extends LostFoundException {
    public UnauthorizedClaimException(String message) {
        super(message, "ERR_UNAUTHORIZED_CLAIM");
    }
}
