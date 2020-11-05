package com.bld.project.system.block.service;

import com.bld.common.utils.StringUtils;
import com.bld.common.utils.security.ShiroUtils;
import com.bld.framework.web.domain.ResultListInfo;
import com.bld.project.system.block.mapper.BlockDeviceMapper;
import com.bld.project.system.block.mapper.BlockHashMapper;
import com.bld.project.system.block.model.BlockDevice;
import com.bld.project.system.block.model.BlockHash;
import com.bld.project.system.user.domain.ThingsboardUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BlockHashServiceImpl implements BlockHashService {
    @Resource
    private BlockHashMapper blockHashMapper;
    @Resource
    private BlockDeviceMapper blockDeviceMapper;

    @Override
    public ResultListInfo select(BlockHash bh) {
        ThingsboardUser tbUser = ShiroUtils.getSysUser().getThingsboardUser();
        String chipId = bh.getChipId();
        String toWallet = bh.getToWallet();
        String fromWallet = bh.getFromWallet();
        if (!tbUser.isSysAdmin()) {
            BlockDevice bd = new BlockDevice(chipId);
            bd.setTenantId(tbUser.getTenantId().getId());
            bd.setPageNum(0);
            bd.setPageSize(10);
            if (!tbUser.isTenantAdmin()) {
                bd.setCustomerId(tbUser.getCustomerId().getId());
            }
            List<BlockDevice> select = blockDeviceMapper.select(bd);
            if (select == null || select.size() < 1) {
                return ResultListInfo.success(null, "", 0, 0, 0);
            }
        }
        if (StringUtils.isNullString(chipId) && StringUtils.isNullString(toWallet) && StringUtils.isNullString(fromWallet)) {
            return ResultListInfo.error("请输出查询参数");
        }
        BlockHash selBh = new BlockHash(chipId, toWallet, fromWallet);
        selBh.setPageNum((bh.getPageNum() - 1) * bh.getPageSize());
        selBh.setPageSize(bh.getPageSize());
        List<BlockHash> select = blockHashMapper.select(selBh);
        Integer integer = blockHashMapper.selectCount(selBh);
        return ResultListInfo.success(select, "", integer, bh.getPageNum(), bh.getPageSize());
    }

    @Override
    public ResultListInfo searchTableMoneyData(BlockHash bh) {
        Map<String, Object> map = new HashMap<>();
        bh.setPageNum((bh.getPageNum() - 1) * bh.getPageSize());
        //查詢數據
        List<BlockHash> blockHash = blockHashMapper.searchTableMoneyData(bh);
        //查詢總條數
        Integer integer = blockHashMapper.searchTableMoneyDataCount(bh);
        map.put("data", blockHash);
        return ResultListInfo.success(blockHash, "", integer, 0, 0);
    }
}
