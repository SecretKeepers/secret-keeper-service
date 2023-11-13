package com.secretkeeper.exceptions;

public class MasterKeyNotPresentException extends Exception {
    public MasterKeyNotPresentException(String msg) {
        super(msg);
    }
}
