package com.bld.common.exception;

/**
 * @author SOFAS
 * @date 2020/5/7
 * @directions  倍来电自定义错误
*/
public class BldException extends Exception{
    private String message;

    public BldException(String msg) {
        this.message = msg;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
