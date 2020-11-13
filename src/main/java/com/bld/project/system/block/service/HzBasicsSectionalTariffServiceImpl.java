package com.bld.project.system.block.service;

import com.bld.framework.web.domain.ResultListInfo;
import com.bld.project.system.block.mapper.HzBasicsSectionalTariffMapper;
import com.bld.project.system.block.model.BasicsSectionalTariffBo;
import jnr.ffi.annotations.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HzBasicsSectionalTariffServiceImpl {

    @Autowired
    private HzBasicsSectionalTariffMapper hzBasicsSectionalTariffMapper;

    /**
     * 查询尖峰平谷分段电价表
     * @param map
     * @return
     * @throws Exception
     */
    public ResultListInfo searchTableData(Map<String,String> map) throws Exception{
        Integer pageNum = Integer.parseInt(map.get("pageNum"));
        pageNum = (pageNum-1) * pageNum;
        map.put("pageNum",pageNum+"");
        List<BasicsSectionalTariffBo> basicsSectionalTariffBos = hzBasicsSectionalTariffMapper.searchTableData(map);
        Integer integer = hzBasicsSectionalTariffMapper.countSearchTableData(map);
        return ResultListInfo.success(basicsSectionalTariffBos, "查询成功", integer, 0, 0);
    }

    public ResultListInfo deleteOperationByIds(BasicsSectionalTariffBo b){
        Integer integer = hzBasicsSectionalTariffMapper.deleteOperationByIds(b);
        return ResultListInfo.success(integer, "删除成功", 0, 0, 0);
    }
}
