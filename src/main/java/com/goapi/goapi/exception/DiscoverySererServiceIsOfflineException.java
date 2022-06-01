package com.goapi.goapi.exception;

/**
 * @author Daniil Dmitrochenkov
 **/
public class DiscoverySererServiceIsOfflineException extends RuntimeException {

    public DiscoverySererServiceIsOfflineException() {
        super("Databases discovery app server services is offline!");
    }
}
