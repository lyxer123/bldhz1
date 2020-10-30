package com.bld.project.system.block.mapper;

import com.bld.project.system.block.model.BlockDevice;
import com.bld.project.system.block.model.BlockHash;

import java.util.List;

/**
 * @author SOFAS
 * @date 2020/7/3
 * @directions  区块链交易记录
*/
public interface BlockHashMapper {
    /**
     * @author SOFAS
     * @date   2020/7/3
     * @directions  添加
     * @param bh  参数
     * @return  int
     */
    int add(BlockHash bh);
    /**
     * @author SOFAS
     * @date   2020/7/3
     * @directions  删除
     * @param bh  条件
     * @return  int
     */
    int delete(BlockDevice bh);
    /**
     * @author SOFAS
     * @date   2020/7/2
     * @directions  修改
     * @param update  修改参数
     * @param where  修改条件
     * @return  int
     */
    int update(BlockHash update, BlockHash where);
    /**
     * @author SOFAS
     * @date   2020/7/2
     * @directions  查询
     * @param bh  查询条件
     * @return  java.util.List<com.bld.project.system.block.model.BlockHash>
     */
    List<BlockHash> select(BlockHash bh);

    Integer selectCount(BlockHash bh);

    List<BlockHash> searchTableMoneyData(BlockHash bh);

    Integer searchTableMoneyDataCount(BlockHash bh);
}
