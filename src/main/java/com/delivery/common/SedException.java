package com.delivery.common;

import com.delivery.common.response.ErrorCode;

/**
 * @author finderlo
 * @date 20/04/2017
 */
public class SedException extends RuntimeException {

    ErrorCode errorCode;

    public SedException(ErrorCode error) {
        errorCode = error;
    }
}
