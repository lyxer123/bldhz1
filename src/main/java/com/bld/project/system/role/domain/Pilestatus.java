package com.bld.project.system.role.domain;


import java.sql.Timestamp;

public class Pilestatus {

  private long id;
  private String sn;
  private double ua;
  private double ub;
  private double uc;
  private double ia;
  private double ib;
  private double ic;
  private double pa;
  private double pb;
  private double pc;
  private double pf;
  private double gf;
  private double pt;
  private double en;
  private String w;
  private String e;
  private java.sql.Timestamp time;
  private String rssi;
  private String chipId;

  public String getW() {
    return w;
  }

  public void setW(String w) {
    this.w = w;
  }

  public String getE() {
    return e;
  }

  public void setE(String e) {
    this.e = e;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public String getSn() {
    return sn;
  }

  public void setSn(String sn) {
    this.sn = sn;
  }


  public double getUa() {
    return ua;
  }

  public void setUa(double ua) {
    this.ua = ua;
  }


  public double getUb() {
    return ub;
  }

  public void setUb(double ub) {
    this.ub = ub;
  }


  public double getUc() {
    return uc;
  }

  public void setUc(double uc) {
    this.uc = uc;
  }


  public double getIa() {
    return ia;
  }

  public void setIa(double ia) {
    this.ia = ia;
  }


  public double getIb() {
    return ib;
  }

  public void setIb(double ib) {
    this.ib = ib;
  }


  public double getIc() {
    return ic;
  }

  public void setIc(double ic) {
    this.ic = ic;
  }


  public double getPa() {
    return pa;
  }

  public void setPa(double pa) {
    this.pa = pa;
  }


  public double getPb() {
    return pb;
  }

  public void setPb(double pb) {
    this.pb = pb;
  }


  public double getPc() {
    return pc;
  }

  public void setPc(double pc) {
    this.pc = pc;
  }


  public double getPf() {
    return pf;
  }

  public void setPf(double pf) {
    this.pf = pf;
  }


  public double getGf() {
    return gf;
  }

  public void setGf(double gf) {
    this.gf = gf;
  }


  public double getPt() {
    return pt;
  }

  public void setPt(double pt) {
    this.pt = pt;
  }


  public double getEn() {
    return en;
  }

  public void setEn(double en) {
    this.en = en;
  }


  public java.sql.Timestamp getTime() {
    return time;
  }

  public void setTime(java.sql.Timestamp time) {
    this.time = time;
  }


  public String getRssi() {
    return rssi;
  }

  public void setRssi(String rssi) {
    this.rssi = rssi;
  }


  public String getChipId() {
    return chipId;
  }

  public void setChipId(String chipId) {
    this.chipId = chipId;
  }

  public Pilestatus() {
  }

  public Pilestatus(long id, String sn, double ua, double ub, double uc, double ia, double ib, double ic, double pa, double pb, double pc, double pf, double gf, double pt, double en, String w, String e, Timestamp time, String rssi, String chipId) {
    this.id = id;
    this.sn = sn;
    this.ua = ua;
    this.ub = ub;
    this.uc = uc;
    this.ia = ia;
    this.ib = ib;
    this.ic = ic;
    this.pa = pa;
    this.pb = pb;
    this.pc = pc;
    this.pf = pf;
    this.gf = gf;
    this.pt = pt;
    this.en = en;
    this.w = w;
    this.e = e;
    this.time = time;
    this.rssi = rssi;
    this.chipId = chipId;
  }

  @Override
  public String toString() {
    return "Pilestatus{" +
            "id=" + id +
            ", sn='" + sn + '\'' +
            ", ua=" + ua +
            ", ub=" + ub +
            ", uc=" + uc +
            ", ia=" + ia +
            ", ib=" + ib +
            ", ic=" + ic +
            ", pa=" + pa +
            ", pb=" + pb +
            ", pc=" + pc +
            ", pf=" + pf +
            ", gf=" + gf +
            ", pt=" + pt +
            ", en=" + en +
            ", w='" + w + '\'' +
            ", e='" + e + '\'' +
            ", time=" + time +
            ", rssi='" + rssi + '\'' +
            ", chipId='" + chipId + '\'' +
            '}';
  }
}
