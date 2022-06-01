package com.goapi.goapi.controller;

import io.grpc.Status;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * @author Daniil Dmitrochenkov
 **/

@ControllerAdvice
public class GrpcServiceControllerAdvice {


    @ExceptionHandler(io.grpc.StatusRuntimeException.class)
    public ResponseEntity handleStatusRuntimeException(io.grpc.StatusRuntimeException e, WebRequest request) {
        Status status = e.getStatus();
        Status.Code code = status.getCode();
        HttpStatus resultStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        switch (code) {
            case OK -> resultStatus = HttpStatus.OK;
            case CANCELLED, UNKNOWN, RESOURCE_EXHAUSTED, ABORTED, OUT_OF_RANGE, INTERNAL, DATA_LOSS ->
                resultStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            case INVALID_ARGUMENT -> resultStatus = HttpStatus.BAD_REQUEST;
            case NOT_FOUND -> resultStatus = HttpStatus.NOT_FOUND;
            case ALREADY_EXISTS -> resultStatus = HttpStatus.CONFLICT;
            case PERMISSION_DENIED, UNAUTHENTICATED -> resultStatus = HttpStatus.UNAUTHORIZED;
            case DEADLINE_EXCEEDED -> resultStatus = HttpStatus.GATEWAY_TIMEOUT;
            case FAILED_PRECONDITION -> resultStatus = HttpStatus.BAD_GATEWAY;
            case UNIMPLEMENTED -> resultStatus = HttpStatus.METHOD_NOT_ALLOWED;
            case UNAVAILABLE -> resultStatus = HttpStatus.SERVICE_UNAVAILABLE;
        }
        return ResponseEntity.status(resultStatus).build();
    }

}
