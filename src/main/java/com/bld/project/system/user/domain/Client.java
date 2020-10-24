package com.bld.project.system.user.domain;

public class Client {

    private String id;

    private String name;

    private String adress;

    private String phone;

    public Client(String id, String name, String adress, String phone) {
        this.id = id;
        this.name = name;
        this.adress = adress;
        this.phone = phone;
    }

    public Client() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
