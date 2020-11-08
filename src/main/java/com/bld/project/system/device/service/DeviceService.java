package com.bld.project.system.device.service;

import com.alibaba.fastjson.JSONObject;
import com.bld.framework.web.domain.ResultInfo;
import com.bld.project.system.device.model.TbDevice;
import org.web3j.crypto.CipherException;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;

/**
 * @author SOFAS
 * @date 2020/5/29
 * @directions  设备管理逻辑层
*/
public interface DeviceService {
    /**
     * @author SOFAS
     * @date   2020/5/29
     * @directions  查询设备列表
     * @param limit  每页查询数量
     * @param search  查询调教
     * @return  com.bld.framework.web.domain.ResultInfo
     */
    ResultInfo<List<TbDevice>> searchDevice(int limit, String search, String tbToken,String textOffset,String  idOffset);
    /**
     * @author SOFAS
     * @date   2020/7/3
     * @directions  根据设备被分配客户查询设备
     * @param limit  每页数量
     * @param search  查询条件
     * @param id  分配客户id
     * @return  com.bld.framework.web.domain.ResultInfo
     */
    ResultInfo getDeviceListByCustomer(int limit, String search, String id);
    /**
     * @author SOFAS
     * @date   2020/5/29
     * @directions  删除分配设备
     * @param deviceId  设备id
     * @return  com.bld.framework.web.domain.ResultInfo
     */
    ResultInfo delDeviceDistribution(String deviceId);
    /**
     * @author SOFAS
     * @date   2020/5/29
     * @directions  设备管理
     * @param customerId  客户id
     * @param deviceId  设备id
     * @return  com.bld.framework.web.domain.ResultInfo
     */
    ResultInfo deviceDistribution(String customerId, String deviceId);
    /**
     * @author SOFAS
     * @date   2020/5/29
     * @directions  添加新设备
     * @param name  名称
     * @param type  类型
     * @param label  标签
     * @param description  说明
     * @param gateway
     * @param tbToken
     * @return  com.bld.framework.web.domain.ResultInfo
     */
    ResultInfo addDevice(String name, String type, String label, String description, String gateway, String tbToken) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchProviderException, CipherException, IOException;
    /**
     * @author SOFAS
     * @date   2020/5/29
     * @directions  设备自动添加添加
     * @param name  名称
     * @param type  类型
     * @param tbToken 添加租户token
     * @param workingVersion
     * @param isBesu
     * @return  com.bld.framework.web.domain.ResultInfo
     */
    ResultInfo autoAddDevice(String name, String type, String tbToken, String workingVersion, String sn1, String sn2, boolean isBesu) throws NoSuchProviderException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, CipherException, IOException;

    ResultInfo updateDevice(JSONObject json);
    /**
     * @author SOFAS
     * @date   2020/5/29
     * @directions  获取设备类型
     * @return  com.bld.framework.web.domain.ResultInfo
     */
    ResultInfo getDeviceTypes();
    /**
     * @author SOFAS
     * @date   2020/5/29
     * @directions  删除id
     * @param deviceId  设备id
     * @return  com.bld.framework.web.domain.ResultInfo
     */
    ResultInfo delDevice(String deviceId);
    /**
     * @author SOFAS
     * @date   2020/6/8
     * @directions  获取设备token
     * @return  com.bld.framework.web.domain.ResultInfo
     */
    ResultInfo<String> getDeviceToken(String deviceId, String token);
    /**
     * @author SOFAS
     * @date   2020/6/9
     * @directions  公开设备
     * @param deviceId  设备id
     * @return  com.bld.framework.web.domain.ResultInfo
     */
    ResultInfo publicDevice(String deviceId);
    /**
     * @author SOFAS
     * @date   2020/6/11
     * @directions  获取警告
     * @param deviceId
     * @param limit
     * @param startTime
     * @param endTime
     * @param searchStatus
     * @param fetchOriginator
     * @param ascOrder
     * @return  com.bld.framework.web.domain.ResultInfo
     */
    ResultInfo getDeviceAlarm(String deviceId, int limit, long startTime, long endTime, String searchStatus, boolean fetchOriginator, boolean ascOrder);
    /**
     * @author SOFAS
     * @date   2020/6/11
     * @directions  获取事件
     * @param deviceId
     * @param type
     * @param tenantId
     * @param limit
     * @param startTime
     * @param endTime
     * @return  com.bld.framework.web.domain.ResultInfo
     */
    ResultInfo getDeviceEvent(String deviceId, String type, String tenantId, int limit, long startTime, long endTime);
    /**
     * @author SOFAS
     * @date   2020/6/11
     * @directions  查询设备属性
     * @param deviceId  设备id
     * @param type  属性类型（SERVER_SCOPE 服务端属性，SHARED_SCOPE 共享属性）
     * @return  java.lang.String
     */
    ResultInfo getDeviceAttributes(String deviceId, String type);
    /**
     * @author SOFAS
     * @date   2020/6/11
     * @directions  添加新的设备属性
     * @param deviceId  设备id
     * @param attribute
     * @return  com.bld.framework.web.domain.ResultInfo
     */
    ResultInfo addDeviceAttributes(String deviceId, String type, String attribute);

    ResultInfo delDeviceAttributes(String deviceId, String k);

    ResultInfo searchMaintain(String search, String cId, int pageSize, int pageNum);
}
