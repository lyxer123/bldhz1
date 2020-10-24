package com.bld.project.system.role.domain;

public class Pileassets {

    private  String token;

    private  String chpId;

    private  String money;

    private String fromWallet;

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

    public String getChpId() {
        return chpId;
    }

    public void setChpId(String chpId) {
        this.chpId = chpId;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public Pileassets() {

    }

    public Pileassets(String token, String chpId, String money, String fromWallet) {
        this.token = token;
        this.chpId = chpId;
        this.money = money;
        this.fromWallet = fromWallet;
    }

    @Override
    public String toString() {
        return "Pileassets{" +
                "token='" + token + '\'' +
                ", chpId='" + chpId + '\'' +
                ", money='" + money + '\'' +
                ", fromWallet='" + fromWallet + '\'' +
                '}';
    }
}
