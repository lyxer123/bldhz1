package com.bld.project.system.block.model;

import lombok.Data;

import java.util.List;

@Data
public class BasicsSectionalTariffBo  extends  BasicsSectionalTariffPo{
    private String sectionalName;
    //删除用的id
    private List<Integer> ids;
}
