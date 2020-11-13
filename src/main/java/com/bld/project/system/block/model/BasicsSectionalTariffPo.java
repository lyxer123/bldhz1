package com.bld.project.system.block.model;

import jnr.ffi.annotations.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 尖峰平谷分段电价表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasicsSectionalTariffPo {
    private Integer id;
    //分段时间以逗号隔开
    private String timeSectional;
    //分段时间内每度电的价格
    private BigDecimal price;
    //删除状态 0是未删 1是已删除
    private Integer delete;
    //创建时间
    private String createTime;
    //修改时间
    private String modificationTime;
    //时段名称
    private String name;
    //设备名称表的type字段
    private Integer equipmentListType;
}
