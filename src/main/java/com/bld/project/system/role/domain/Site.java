package com.bld.project.system.role.domain;

import java.io.Serializable;

public class Site implements Serializable {

  private long id;
  private String siteName;
  private String gps;
  private String pileassets;
  private String adress;
  private String manager;

  public String getManager() {
    return manager;
  }

  public void setManager(String manager) {
    this.manager = manager;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public String getSiteName() {
    return siteName;
  }

  public void setSiteName(String siteName) {
    this.siteName = siteName;
  }


  public String getGps() {
    return gps;
  }

  public void setGps(String gps) {
    this.gps = gps;
  }


  public String getPileassets() {
    return pileassets;
  }

  public void setPileassets(String pileassets) {
    this.pileassets = pileassets;
  }


  public String getAdress() {
    return adress;
  }

  public void setAdress(String adress) {
    this.adress = adress;
  }

  public Site() {
  }


  public Site(long id, String siteName, String gps, String pileassets, String adress, String manager) {
    this.id = id;
    this.siteName = siteName;
    this.gps = gps;
    this.pileassets = pileassets;
    this.adress = adress;
    this.manager = manager;
  }

  @Override
  public String toString() {
    return "Site{" +
            "id=" + id +
            ", siteName='" + siteName + '\'' +
            ", gps='" + gps + '\'' +
            ", pileassets='" + pileassets + '\'' +
            ", adress='" + adress + '\'' +
            ", manager='" + manager + '\'' +
            '}';
  }
}
