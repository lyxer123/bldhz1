package com.bld.project.system.role.domain;


public class Ota {

  private long id;
  private long updateVersion;
  private String json;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public long getUpdateVersion() {
    return updateVersion;
  }

  public void setUpdateVersion(long updateVersion) {
    this.updateVersion = updateVersion;
  }


  public String getJson() {
    return json;
  }

  public void setJson(String json) {
    this.json = json;
  }

}
