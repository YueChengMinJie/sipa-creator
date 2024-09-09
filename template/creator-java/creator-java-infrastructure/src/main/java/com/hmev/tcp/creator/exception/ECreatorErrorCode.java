package com.hmev.tcp.creator.exception;

import com.sipa.boot.core.constant.SipaConstant;
import com.sipa.boot.core.exception.EProjectModule;
import com.sipa.boot.core.exception.api.IErrorCode;
import com.sipa.boot.core.exception.api.IProjectModule;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author caszhou
 * @date 2023/4/25
 */
@Getter
@AllArgsConstructor
public enum ECreatorErrorCode implements IErrorCode {
    DEFAULT_ERROR("ZZ", SipaConstant.GLOBAL_MSG),

    ;

    private final String code;

    private final String msg;

    @Override
    public IProjectModule getProjectModule() {
        return EProjectModule.BIZ_USERCASE;
    }
}
