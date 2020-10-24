package com.bld.project.system.role.domain;

import com.bld.framework.web.domain.BaseEntity;

import java.util.Date;


public class Device extends BaseEntity {
    //设备管理员
    private String deviceId;
    //创建时间
    private String createdTime;
    //承租管理员
    private String tenantName;
    //所属用户
    private  String customerName;
    //设备名称
    private String deviceName;

    //钱包地址
    private String adress;

    //金额
    private String money;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    //设备类型
    private String deviceType;
    //设备访问令牌
    private  String token;

    //状态
    private String status;

    //token类型
    private String tokenType;

    private String block;

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public Device() {
    }

    @Override
    public String toString() {
        return "Device{" +
                "deviceId='" + deviceId + '\'' +
                ", createdTime='" + createdTime + '\'' +
                ", tenantName='" + tenantName + '\'' +
                ", customerName='" + customerName + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", adress='" + adress + '\'' +
                ", money='" + money + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", token='" + token + '\'' +
                ", status='" + status + '\'' +
                ", tokenType='" + tokenType + '\'' +
                '}';
    }

    public Device(String deviceId, String createdTime, String tenantName, String customerName, String deviceName, String adress, String money, String deviceType, String token, String status, String tokenType) {
        this.deviceId = deviceId;
        this.createdTime = createdTime;
        this.tenantName = tenantName;
        this.customerName = customerName;
        this.deviceName = deviceName;
        this.adress = adress;
        this.money = money;
        this.deviceType = deviceType;
        this.token = token;
        this.status = status;
        this.tokenType = tokenType;
    }
}
