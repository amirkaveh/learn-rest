package com.pinket.exceptions;

public class DBException extends Exception {
    public DBException() {
        super();
    }

    public DBException(String errorMessage) {
        super("DB Problem: " + errorMessage);
    }

    public DBException(Throwable e) {
        super(e);
    }

    public DBException(String errorMessage, Throwable e) {
        super("DB Problem: " + errorMessage, e);
    }
}
