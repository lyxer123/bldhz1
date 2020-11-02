package com.bld.project.system.block.service;

import com.bld.framework.web.domain.ResultListInfo;
import com.bld.project.system.block.model.BlockHash;

import java.util.List;
import java.util.Map;

/**
 * @author SOFAS
 * @date 2020/7/3
 * @directions  设备交易hash
*/
public interface BlockHashService {
    ResultListInfo select(BlockHash bh);

    ResultListInfo searchTableMoneyData(BlockHash bh);
}
