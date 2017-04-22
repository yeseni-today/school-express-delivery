package com.delivery.common;

/**
 * @author finderlo
 * @date 20/04/2017
 */
public class SedException extends RuntimeException {

    ErrorCode errorCode;

    public SedException(ErrorCode error) {
        errorCode = error;
    }


    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
