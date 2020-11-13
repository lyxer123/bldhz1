package com.bld.project.system.block.mapper;

import com.bld.project.system.block.model.BasicsSectionalTariffBo;
import jnr.ffi.annotations.In;
import org.web3j.abi.datatypes.Int;

import java.util.List;
import java.util.Map;

public interface HzBasicsSectionalTariffMapper {

    /**
     * 查询尖峰平谷分段电价表
     * @param map
     * @return
     */
    List<BasicsSectionalTariffBo> searchTableData(Map<String,String> map);

    /**
     * 查询尖峰平谷分段电价表总条数
     * @param map
     * @return
     */
    Integer countSearchTableData(Map<String,String> map);

    Integer deleteOperationByIds(BasicsSectionalTariffBo b);
}
