package com.bld.project.system.block.model;

import lombok.Data;

/**
 * 设备名称表
 */
@Data
public class BasicsEquipmentListPo {
    private Integer id;
    //设备类型名称
    private String name;
    //删除状态 0是未删 1是已删除
    private Integer delete;
    //1,是光伏设备；2是电力电网设备；3是储能设备；4是充电桩设备
    private Integer type;
}
