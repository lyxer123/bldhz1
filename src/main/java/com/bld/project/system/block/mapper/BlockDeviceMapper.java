package com.bld.project.system.block.mapper;

import com.bld.project.system.block.model.BlockDevice;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author SOFAS
 * @date 2020/7/2
 * @directions 区块链设备信息
*/
public interface BlockDeviceMapper {
    /**
     * @author SOFAS
     * @date   2020/7/2
     * @directions  添加
     * @param blockDevice  参数
     * @return  int
     */
    int add(BlockDevice blockDevice);
    /**
     * @author SOFAS
     * @date   2020/7/2
     * @directions  删除
     * @param blockDevice  条件
     * @return  int
     */
    int delete(BlockDevice blockDevice);
    /**
     * @author SOFAS
     * @date   2020/7/2
     * @directions  修改
     * @param update  修改参数
     * @param where  修改条件
     * @return  int
     */
    int update(BlockDevice update, BlockDevice where);
    /**
     * @author SOFAS
     * @date   2020/7/7
     * @directions  修改设备地址
     * @param gps  坐标
     * @param addr  详细地址
     * @param chipId  设备编号
     * @return  int
     */
    int updateGps(@Param("gps") String gps, @Param("addr") String addr, @Param("chipId") String chipId);
    /**
     * @author SOFAS
     * @date   2020/7/2
     * @directions  查询
     * @param blockDevice  查询条件
     * @return  java.util.List<com.bld.project.system.block.model.BlockDevice>
     */
    List<BlockDevice> select(BlockDevice blockDevice);

    /*查询总条数*/
    Integer selectCount(BlockDevice blockDevice);
}
