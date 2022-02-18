package com.spurious.bilibili.dao.domain.exception;

import lombok.Data;

@Data
public class ConditionException extends RuntimeException{

    private static final Long serialVersionUID = 1L;

    private String code;

    public ConditionException(String code, String msg) {
        super(msg);
        this.code = code;
    }

    public ConditionException(String msg) {
        super(msg);
        code = "500";
    }

}
