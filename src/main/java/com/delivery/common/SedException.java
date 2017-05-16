package com.delivery.common;

import org.hibernate.tool.schema.internal.StandardUniqueKeyExporter;

/**
 * @author finderlo
 * @date 20/04/2017
 */
public class SedException extends RuntimeException {


    private int status = 500;


    public SedException(int status, String message) {
        this(status, message, null);
    }


    public SedException(int status, String message, Exception e) {
        super(message, e);
        this.status = status;
    }

    public SedException(ErrorCode errorCode, Exception e) {
        this(errorCode.getCode(),errorCode.getDescription(),e);
    }

    public SedException(ErrorCode errorCode) {
        this(errorCode,null);
    }


    public int getStatus() {
        return status;
    }
}
