package com.bld.project.system.block.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.bld.common.utils.StringUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author SOFAS
 * @date 2020/7/2
 * @directions  区块链设备信息
*/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("block_device")
public class BlockDevice implements Serializable {
    /**
     * 主键
     */
    @TableId
    private String id;
    /**
     * 设备id
     */
    private String deviceId;
    /**
     * 拥有设备的租户id
     */
    private String tenantId;
    /**
     * 被分配了设备的客户id
     */
    private String customerId;
    /**
     * 设备标识
     */
    private String chipId;
    /**
     * 设备程序版本
     */
    private String deviceVersion;
    /**
     * 设备token
     */
    private String deviceToken;
    /**
     * 转账钱包
     */
    private String toWallet;
    /**
     * 设备钱包
     */
    private String deviceWallet;
    /**
     * 设备地址坐标
     */
    private String gps;
    /**
     * 设备地址
     */
    private String addr;
    /**
     * 设备交易今日历史
     */
    private String transactionHistory;
    /**
     * 设备钱包余额
     */
    private Long deviceMoney;
    /**
     * 添加时间
     */
    private Date addTime;
    /**
     * 最后更新时间
     */
    private Date updateTime;
    /**
     * 表号1
     */
    private String sn1;
    /**
     * 表号2
     */
    private String sn2;

//    --------------------查询用字段---------------------

    /**
     * limit第一个参数， 查询起始行数
     */
    @TableField(exist = false)
    private Integer pageNum;
    /**
     * limit第二个参数， 查询数量
     */
    @TableField(exist = false)
    private Integer pageSize;
    /**
     * 名称模糊查询
     */
    private String search;
    /**
     * 是否只查询gps信息
     */
    @TableField(exist = false)
    private boolean selectGps = false;

//    -------------------------------展示用字段-----------------------------------------
    /**
     * 被分配的客户名称
     */
    private String customerName;
    /**
     * 是否是besu测试
     */
    @TableField(exist = false)
    private Boolean isBesu = false;

    public void setSearch(String search) {
        if (search != null){
            this.search = search + "%";
        }
    }

    public BlockDevice() { }

    public BlockDevice(String chipId) {
        this.chipId = chipId;
    }

    public BlockDevice(int pageNum, int pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public BlockDevice(String deviceId, String tenantId, String customerId, String chipId) {
        this.deviceId = deviceId;
        this.tenantId = tenantId;
        this.customerId = customerId;
        this.chipId = chipId;
    }

    public BlockDevice(String deviceId, String tenantId, String customerId, String chipId, String deviceToken, String deviceVersion, String sn1, String sn2) {
        this.deviceId = deviceId;
        this.tenantId = tenantId;
        this.customerId = customerId;
        this.chipId = chipId;
        this.deviceToken = deviceToken;
        this.deviceVersion = deviceVersion;
        this.sn1 = sn1;
        this.sn2 = sn2;
    }

    public BlockDevice(String deviceId, String tenantId, String customerId, String chipId, String deviceToken, String deviceVersion, String sn1, String sn2, boolean isBesu) {
        this.deviceId = deviceId;
        this.tenantId = tenantId;
        this.customerId = customerId;
        this.chipId = chipId;
        this.deviceToken = deviceToken;
        this.deviceVersion = deviceVersion;
        this.sn1 = sn1;
        this.sn2 = sn2;
        this.isBesu = isBesu;
    }

    public BlockDevice(String deviceId, String tenantId, String customerId, String chipId, String deviceVersion, String deviceToken, String toWallet, String deviceWallet, String hashHistory, String transactionHistory, long deviceMoney) {
        this.deviceId = deviceId;
        this.tenantId = tenantId;
        this.customerId = customerId;
        this.chipId = chipId;
        this.deviceVersion = deviceVersion;
        this.deviceToken = deviceToken;
        this.toWallet = toWallet;
        this.deviceWallet = deviceWallet;
        this.gps = hashHistory;
        this.transactionHistory = transactionHistory;
        this.deviceMoney = deviceMoney;
    }

    public String addCheck(){
        if (StringUtils.isNullString(deviceId)){
            return "设备id不能为空";
        }
        if (StringUtils.isNullString(tenantId)){
            return "租户id不能为空";
        }
        if (StringUtils.isNullString(customerId)){
            return "被分配的客户id不能为空";
        }
        if (StringUtils.isNullString(chipId)){
            return "chipId不能为空";
        }
        return null;
    }
}
