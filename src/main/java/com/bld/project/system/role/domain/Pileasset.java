package com.bld.project.system.role.domain;


import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class Pileasset implements Serializable {
  private Integer id;
  private String chipId;
  private String type;
  private long spicapacity;
  private String workingVersion;
  private String updateVersion;
  private String ccid;
  private String imei;
  private String sn1;
  private String sn2;
  private String sd;
  private String toWallet;
  private String fromWallet;
  private String token;
  private Date time;
  private String gps;
  private String pd;
  private String op;
  private String sp;
  private String customer;
  private String multiMeter;
  private String deviceId;
  private String peerUrl;
  private String money;
  private String blockData;
  private String status;


  public String getBlockData() {
    return blockData;
  }

  public void setBlockData(String blockData) {
    this.blockData = blockData;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }


  public Pileasset() {

  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getChipId() {
    return chipId;
  }

  public void setChipId(String chipId) {
    this.chipId = chipId;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public long getSpicapacity() {
    return spicapacity;
  }

  public void setSpicapacity(long spicapacity) {
    this.spicapacity = spicapacity;
  }

  public String getWorkingVersion() {
    return workingVersion;
  }

  public void setWorkingVersion(String workingVersion) {
    this.workingVersion = workingVersion;
  }

  public String getUpdateVersion() {
    return updateVersion;
  }

  public void setUpdateVersion(String updateVersion) {
    this.updateVersion = updateVersion;
  }

  public String getCcid() {
    return ccid;
  }

  public void setCcid(String ccid) {
    this.ccid = ccid;
  }

  public String getImei() {
    return imei;
  }

  public void setImei(String imei) {
    this.imei = imei;
  }

  public String getSn1() {
    return sn1;
  }

  public void setSn1(String sn1) {
    this.sn1 = sn1;
  }

  public String getSn2() {
    return sn2;
  }

  public void setSn2(String sn2) {
    this.sn2 = sn2;
  }

  public String getSd() {
    return sd;
  }

  public void setSd(String sd) {
    this.sd = sd;
  }

  public String getToWallet() {
    return toWallet;
  }

  public void setToWallet(String toWallet) {
    this.toWallet = toWallet;
  }

  public String getFromWallet() {
    return fromWallet;
  }

  public void setFromWallet(String fromWallet) {
    this.fromWallet = fromWallet;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Date getTime() {
    return time;
  }

  public void setTime(Date time) {
    this.time = time;
  }

  public String getGps() {
    return gps;
  }

  public void setGps(String gps) {
    this.gps = gps;
  }

  public String getPd() {
    return pd;
  }

  public void setPd(String pd) {
    this.pd = pd;
  }

  public String getOp() {
    return op;
  }

  public void setOp(String op) {
    this.op = op;
  }

  public String getSp() {
    return sp;
  }

  public void setSp(String sp) {
    this.sp = sp;
  }

  public String getCustomer() {
    return customer;
  }

  public void setCustomer(String customer) {
    this.customer = customer;
  }

  public String getMultiMeter() {
    return multiMeter;
  }

  public void setMultiMeter(String multiMeter) {
    this.multiMeter = multiMeter;
  }

  public String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }

  public String getPeerUrl() {
    return peerUrl;
  }

  public void setPeerUrl(String peerUrl) {
    this.peerUrl = peerUrl;
  }

  public String getMoney() {
    return money;
  }

  public void setMoney(String money) {
    this.money = money;
  }

  public Pileasset(Integer id, String chipId, String type, long spicapacity, String workingVersion, String updateVersion, String ccid, String imei, String sn1, String sn2, String sd, String toWallet, String fromWallet, String token, Date time, String gps, String pd, String op, String sp, String customer, String multiMeter, String deviceId, String peerUrl, String money, String blockData, String status) {
    this.id = id;
    this.chipId = chipId;
    this.type = type;
    this.spicapacity = spicapacity;
    this.workingVersion = workingVersion;
    this.updateVersion = updateVersion;
    this.ccid = ccid;
    this.imei = imei;
    this.sn1 = sn1;
    this.sn2 = sn2;
    this.sd = sd;
    this.toWallet = toWallet;
    this.fromWallet = fromWallet;
    this.token = token;
    this.time = time;
    this.gps = gps;
    this.pd = pd;
    this.op = op;
    this.sp = sp;
    this.customer = customer;
    this.multiMeter = multiMeter;
    this.deviceId = deviceId;
    this.peerUrl = peerUrl;
    this.money = money;
    this.blockData = blockData;
    this.status = status;
  }

  @Override
  public String toString() {
    return "Pileasset{" +
            "id=" + id +
            ", chipId='" + chipId + '\'' +
            ", type='" + type + '\'' +
            ", spicapacity=" + spicapacity +
            ", workingVersion='" + workingVersion + '\'' +
            ", updateVersion='" + updateVersion + '\'' +
            ", ccid='" + ccid + '\'' +
            ", imei='" + imei + '\'' +
            ", sn1='" + sn1 + '\'' +
            ", sn2='" + sn2 + '\'' +
            ", sd='" + sd + '\'' +
            ", toWallet='" + toWallet + '\'' +
            ", fromWallet='" + fromWallet + '\'' +
            ", token='" + token + '\'' +
            ", time=" + time +
            ", gps='" + gps + '\'' +
            ", pd='" + pd + '\'' +
            ", op='" + op + '\'' +
            ", sp='" + sp + '\'' +
            ", customer='" + customer + '\'' +
            ", multiMeter='" + multiMeter + '\'' +
            ", deviceId='" + deviceId + '\'' +
            ", peerUrl='" + peerUrl + '\'' +
            ", money='" + money + '\'' +
            ", blockData='" + blockData + '\'' +
            ", status='" + status + '\'' +
            '}';
  }
}
