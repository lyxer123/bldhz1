package com.bld.framework.web.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Results<T> implements Serializable {
    @JsonProperty("SmartMeter1")
    private String SmartMeter1;
    @JsonProperty("SmartMeter2")
    private String SmartMeter2;
    @JsonProperty("Token")
    private String Token;
    @JsonProperty("FromWallet")
    private String FromWallet;
    @JsonProperty("ToWallet")
    private String ToWallet;


    public Results() {
    }

    public Results(String smartMeter1, String smartMeter2, String token, String fromWallet, String toWallet) {
        SmartMeter1 = smartMeter1;
        SmartMeter2 = smartMeter2;
        Token = token;
        FromWallet = fromWallet;
        ToWallet = toWallet;
    }
    @JsonIgnore
    public String getSmartMeter1() {
        return SmartMeter1;
    }
    @JsonIgnore
    public void setSmartMeter1(String smartMeter1) {
        SmartMeter1 = smartMeter1;
    }
    @JsonIgnore
    public String getSmartMeter2() {
        return SmartMeter2;
    }
    @JsonIgnore
    public void setSmartMeter2(String smartMeter2) {
        SmartMeter2 = smartMeter2;
    }
    @JsonIgnore
    public String getToken() {
        return Token;
    }
    @JsonIgnore
    public void setToken(String token) {
        Token = token;
    }
    @JsonIgnore
    public String getFromWallet() {
        return FromWallet;
    }
    @JsonIgnore
    public void setFromWallet(String fromWallet) {
        FromWallet = fromWallet;
    }
    @JsonIgnore
    public String getToWallet() {
        return ToWallet;
    }
    @JsonIgnore
    public void setToWallet(String toWallet) {
        ToWallet = toWallet;
    }



}
