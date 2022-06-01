package com.goapi.goapi.exception;

/**
 * @author Daniil Dmitrochenkov
 **/
public class DiscoverySererServiceConnectionException extends RuntimeException {

    public DiscoverySererServiceConnectionException(Throwable e) {
        super("Can't connect databases discovery app services!", e);
    }
}
