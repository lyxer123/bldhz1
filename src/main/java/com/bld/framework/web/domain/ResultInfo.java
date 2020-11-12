package com.bld.framework.web.domain;

import com.bld.common.utils.StringUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author SOFAS
 * @date 2020/4/27
 * @directions  返回实体
*/
@Data
public class ResultInfo<T> implements Serializable {
    /**
     * 返回数据
     */
    private T data;
    /**
     * 返回状态码
     */
    private int code;
    /**
     * 返回信息
     * @see Type
     */
    private String message;
    /**
     * 是否成功
     */
    private boolean success;

    private  String textOffset;

    private String idOffset;

    public ResultInfo() { }

    public ResultInfo(T data, int code, String message, boolean success) {
        this.data = data;
        this.code = code;
        this.message = message;
        this.success = success;
    }
    public ResultInfo(T data, int code, String message, boolean success,String textOffset,String idOffset) {
        this.data = data;
        this.code = code;
        this.message = message;
        this.success = success;
        this.textOffset =textOffset;
        this.idOffset =idOffset;
    }

    public static<T> ResultInfo success(T data){
        return success(data, "操作成功");
    }

    public static<T> ResultInfo success(T data, String message){
        return success(data, 200, message);
    }
    public static<T> ResultInfo success(T data, String message,String textOffset,String idOffset){
        return success(data, 200, message,textOffset,idOffset);
    }

    public static<T> ResultInfo success(T data, int code, String message){
        return new ResultInfo<>(data, code, message, true);
    }
    public static<T> ResultInfo success(T data, int code, String message,String textOffset,String idOffset){
        return new ResultInfo<>(data, code, message, true,textOffset,idOffset);
    }

    public static<T> ResultInfo error(T data){
        return error(data, "操作失败");
    }

    public static<T> ResultInfo error(String message){
        return error(null, message);
    }

    public static<T> ResultInfo error(T data, String message){
        return error(data, Type.ERROR.value, message);
    }

    public static<T> ResultInfo error(int code, String message){
        return new ResultInfo<>(null, code, message, false);
    }

    public static<T> ResultInfo error(T data, int code, String message){
        return new ResultInfo<>(data, code, message, false);
    }

    public static<T> ResultInfo ret(T data){
        return StringUtils.isNullString(data) ? ResultInfo.error("没有查询到数据") : ResultInfo.success(data);
    }

    public enum Type {
        /** 成功 */
        SUCCESS(200),
        /** 警告 */
        WARN(301),
        /** 错误 */
        ERROR(500);
        private final int value;

        Type(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }
    }
}
