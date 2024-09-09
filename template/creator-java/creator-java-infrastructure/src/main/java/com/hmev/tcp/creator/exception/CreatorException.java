package com.hmev.tcp.creator.exception;

import com.sipa.boot.core.exception.BaseException;
import com.sipa.boot.core.exception.EProjectModule;
import com.sipa.boot.core.exception.ErrorInfo;
import com.sipa.boot.core.exception.api.IErrorCode;
import com.sipa.boot.core.exception.api.IProjectModule;

/**
 * @author caszhou
 * @date 2023/4/24
 */
public class CreatorException extends BaseException {
    private static final long serialVersionUID = 9199841513953754011L;

    CreatorException(String message) {
        super(message);
    }

    CreatorException(String message, Throwable cause) {
        super(message, cause);
    }

    CreatorException(Throwable cause) {
        super(cause);
    }

    CreatorException(ErrorInfo errorInfo) {
        super(errorInfo);
    }

    CreatorException(IErrorCode errorCode) {
        super(errorCode);
    }

    CreatorException(IErrorCode errorCode, Object... args) {
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
