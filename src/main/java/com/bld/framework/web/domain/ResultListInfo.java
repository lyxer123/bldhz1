package com.bld.framework.web.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResultListInfo<T> extends ResultInfo {
    private long total;
    private int pageNum;
    private int pageSize;

    public ResultListInfo() { }

    public ResultListInfo(Object data, int code, String message, boolean success, long total, int pageNum, int pageSize) {
        super(data, code, message, success);
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public static ResultListInfo success(Object data, String message, long total, int pageNum, int pageSize){
        return new ResultListInfo(data, 200, message, true, total, pageNum, pageSize);
    }

    public static ResultListInfo error( String message){
        return new ResultListInfo(null, 500, message, false, 0, 0, 0);
    }

    public static ResultListInfo error( String message, int pageNum, int pageSize){
        return new ResultListInfo(null, 500, message, false, 0, pageNum, pageSize);
    }
}
