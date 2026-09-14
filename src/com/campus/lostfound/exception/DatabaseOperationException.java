package com.campus.lostfound.exception;

/**
 * 🗄️ DatabaseOperationException
 * Demonstrates Unit 3: Exception wrapping JDBC/SQL errors.
 */
public class DatabaseOperationException extends LostFoundException {
    public DatabaseOperationException(String message, Throwable cause) {
        super(message, cause, "ERR_DATABASE_FAILURE");
    }
}
