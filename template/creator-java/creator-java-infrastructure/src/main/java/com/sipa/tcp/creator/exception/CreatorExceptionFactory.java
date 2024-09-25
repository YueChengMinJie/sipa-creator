package com.sipa.tcp.creator.exception;

import com.sipa.boot.core.exception.api.IErrorCode;

/**
 * @author caszhou
 * @date 2023/2/14
 */
public class CreatorExceptionFactory {
    public static CreatorRuntimeException bizException(String errorMessage) {
        return new CreatorRuntimeException(errorMessage);
    }

    public static CreatorRuntimeException bizException(IErrorCode errorCode) {
        return new CreatorRuntimeException(errorCode);
    }

    public static CreatorRuntimeException bizException(String errorMessage, Throwable e) {
        return new CreatorRuntimeException(errorMessage, e);
    }

    public static CreatorRuntimeException bizException(IErrorCode errorCode, Throwable e) {
        return new CreatorRuntimeException(errorCode, e);
    }

    public static CreatorException sysException(String errorMessage) {
        return new CreatorException(errorMessage);
    }

    public static CreatorException sysException(IErrorCode errorCode) {
        return new CreatorException(errorCode);
    }

    public static CreatorException sysException(String errorMessage, Throwable e) {
        return new CreatorException(errorMessage, e);
    }

    public static CreatorException sysException(IErrorCode errorCode, Throwable e) {
        return new CreatorException(errorCode, e);
    }
}
