package com.bld.project.system.role.domain;

import java.io.Serializable;
import java.util.Date;

public class Telemetry implements Serializable {
    private String key;

    private String ts;

    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Telemetry{" +
                "key='" + key + '\'' +
                ", ts=" + ts +
                ", value='" + value + '\'' +
                '}';
    }

    public Telemetry(String key, String ts, String value) {
        this.key = key;
        this.ts = ts;
        this.value = value;
    }

    public Telemetry() {
    }
}
