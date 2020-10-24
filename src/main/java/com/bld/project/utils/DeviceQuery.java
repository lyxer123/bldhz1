package com.bld.project.utils;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.bld.common.utils.StringUtils;
import lombok.Data;

/**
 * @author SOFAS
 * @date 2020/6/9
 * @directions  设备信息查询实体
*/
@Data
public class DeviceQuery {
    /**
     * 每页查询数量
     */
    @JsonAlias("limit")
    private int limit;
    /**
     * 查询id
     */
    @JsonAlias("id")
    private String id;
    /**
     * 查询开始时间
     */
    private long startTime;
    /**
     * 查询截止时间
     */
    private long endTime;
    /**
     * 查询状态
     */
    private String searchStatus = "ANY";
    /**
     * 是否查询发起对象
     */
    private boolean fetchOriginator = true;
    /**
     * 是否升序排列
     */
    private boolean ascOrder = false;
    /**
     * 查询类型
     * LC_EVENT  生命周期
     * ERROR  错误
     * STATS 类型统计
     */
    private String type;
    /**
     * 租户ID
     */
    private String tenantId;

    public String checkAlarm(){
        if (StringUtils.isNullString(this.id)){
            return "没有获取到查询id";
        }
        if (0 == this.startTime){
            return "没有获取到开始查询时间";
        }
        if (0 == this.endTime){
            return "没有获取到截止时间";
        }
        if (StringUtils.isNullString(this.searchStatus)){
            return "没有获取到查询状态";
        }
        return null;
    }

    public String checkEvent(){
        if (0 == this.startTime){
            return "没有获取到开始查询时间";
        }
        if (0 == this.endTime){
            return "没有获取到截止时间";
        }
        if (StringUtils.isNullString(this.id)){
            return "没有获取到查询id";
        }
        if (StringUtils.isNullString(this.tenantId)){
            return "没有获取到租户id";
        }
        if (StringUtils.isNullString(type)){
            return "没有获取到查询事件类型";
        }
        return null;
    }
}
