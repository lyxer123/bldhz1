package com.bld.project.system.block.service;

import com.bld.framework.web.domain.ResultInfo;
import com.bld.framework.web.domain.ResultListInfo;
import com.bld.project.system.block.model.BlockDevice;
import org.web3j.crypto.CipherException;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * @author SOFAS
 * @date 2020/7/3
 * @directions 区块链设备信息
*/
public interface BlockDeviceService {
    /**
     * @author SOFAS
     * @date   2020/7/3
     * @directions  查询区块链设备信息
     * @param bd  查询条件
     * @return  com.bld.framework.web.domain.ResultListInfo
     */
    ResultListInfo select(BlockDevice bd);
    /**
     * @author SOFAS
     * @date   2020/7/4
     * @directions  查询设备坐标
     * @param chipId  设备标识
     * @param cId  设备拥有人id
     * @param tId
     * @return  com.bld.framework.web.domain.ResultListInfo
     */
    ResultListInfo searchDeviceGps(String chipId, String cId, String tId);
    /**
     * @author SOFAS
     * @date   2020/7/3
     * @directions  添加新的区块链设备信息
     * @param bd  新的参数
     * @return  ResultInfo
     */
    ResultInfo addBlockDevice(BlockDevice bd, String tbToken) throws NoSuchAlgorithmException, CipherException, InvalidAlgorithmParameterException, NoSuchProviderException, IOException;
    /**
     * @author SOFAS
     * @date   2020/7/3
     * @directions  异步添加
     */
    void asyncAddBlockDevice(BlockDevice bd) throws NoSuchProviderException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, CipherException, IOException;
    /**
     * @author SOFAS
     * @date   2020/7/3
     * @directions  修改区块链设备信息
     * @param updateBd  修改参数
     * @param whereBd  修改条件
     * @return  boolean  是否修改成功
     */
    boolean updateBlockDevice(BlockDevice updateBd, BlockDevice whereBd);
    /**
     * @author SOFAS
     * @date   2020/7/3
     * @directions 异步修改
     */
    void asyncUpdateBlockDevice(BlockDevice updateBd, BlockDevice whereBd);
}
