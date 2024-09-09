package com.hmev.tcp.creator.exception;

import com.sipa.boot.core.exception.BaseRuntimeException;
import com.sipa.boot.core.exception.EProjectModule;
import com.sipa.boot.core.exception.ErrorInfo;
import com.sipa.boot.core.exception.api.IErrorCode;
import com.sipa.boot.core.exception.api.IProjectModule;

/**
 * @author caszhou
 * @date 2023/4/24
 */
public class CreatorRuntimeException extends BaseRuntimeException {
    private static final long serialVersionUID = -1627307972121435644L;

    CreatorRuntimeException(String message) {
        super(message);
    }

    CreatorRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    CreatorRuntimeException(Throwable cause) {
        super(cause);
    }

    CreatorRuntimeException(ErrorInfo errorInfo) {
        super(errorInfo);
    }

    CreatorRuntimeException(IErrorCode errorCode) {
        super(errorCode);
    }

    CreatorRuntimeException(IErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

    @Override
    public IErrorCode getDefaultErrorCode() {
        return ECreatorErrorCode.DEFAULT_ERROR;
    }

    @Override
    public IProjectModule projectModule() {
        return EProjectModule.BIZ_USERCASE;
    }
}
