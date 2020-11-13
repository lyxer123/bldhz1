package com.bld.project.system.block;

import com.bld.framework.web.domain.ResultListInfo;
import com.bld.project.system.block.mapper.HzBlockDeviceMapper;
import com.bld.project.system.block.model.BasicsSectionalTariffBo;
import com.bld.project.system.block.model.BlockHash;
import com.bld.project.system.block.service.HzBasicsSectionalTariffServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("hzBasics")
@RestController
public class HzBasicsSectionalTariffController {

    @Autowired
    private HzBasicsSectionalTariffServiceImpl hzBasicsSectionalTariffService;

    /**
     * 查询尖峰平谷分段电价表
     * @return
     */
    @GetMapping("searchTableData.json")
    public ResultListInfo searchTableData(@RequestParam Map<String,String> map){
        try {
            return hzBasicsSectionalTariffService.searchTableData(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultListInfo.error(null,0,0);
        }
    }

    @PostMapping("deleteOperation.json")
    public ResultListInfo deleteOperationByIds(BasicsSectionalTariffBo b){
        try {
            return hzBasicsSectionalTariffService.deleteOperationByIds(b);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultListInfo.error(null,0,0);
        }
    }
}
