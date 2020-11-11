package com.mashangyou.wanliuprint.api;

public class ServerResponseException extends RuntimeException {
    public ServerResponseException(int errorCode, String cause) {
        super(cause, new Throwable("Server error"));
    }
}
