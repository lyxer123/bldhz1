package com.bld.project.system.role.domain;

import java.io.Serializable;
import java.util.List;

public class Result implements Serializable {

    private Device device;
    private Pileasset pileasset;
    private List<Ota> otas;

    public List<Ota> getOtas() {
        return otas;
    }

    public void setOtas(List<Ota> otas) {
        this.otas = otas;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Pileasset getPileasset() {
        return pileasset;
    }

    public void setPileasset(Pileasset pileasset) {
        this.pileasset = pileasset;
    }

    public Result(Device device, Pileasset pileasset, List<Ota> otas) {
        this.device = device;
        this.pileasset = pileasset;
        this.otas = otas;
    }

    public Result() {
    }
}
