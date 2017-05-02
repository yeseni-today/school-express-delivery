package com.delivery.common;

/**
 * @author finderlo
 * @date 20/04/2017
 */
public class SedException extends RuntimeException {

    ErrorCode errorCode;

    Exception originException;

    public SedException(ErrorCode error) {
        errorCode = error;
    }

    public SedException(ErrorCode error, Exception e) {
        errorCode = error;
        this.originException = e;
    }


    @Override
    public void printStackTrace() {
        if (originException == null) {
            super.printStackTrace();
        } else originException.printStackTrace();
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
