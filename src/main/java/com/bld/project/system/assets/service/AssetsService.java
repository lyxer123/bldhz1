package com.bld.project.system.assets.service;

import com.alibaba.fastjson.JSONObject;
import com.bld.framework.web.domain.ResultInfo;

/**
 * @author SOFAS
 * @date 2020/5/27
 * @directions  资产管理逻辑处理接口
*/
public interface AssetsService {
    /**
     * @author SOFAS
     * @date   2020/5/27
     * @directions  查询资产列表
     * @param limit  每页查询数量
     * @param search  查询条件
     * @return  com.bld.framework.web.domain.ResultInfo
     */
    ResultInfo assetsList(int limit, String search);
    /**
     * @author SOFAS
     * @date   2020/5/27
     * @directions  查询资产类型列表
     * @return  com.bld.framework.web.domain.ResultInfo
     */
    ResultInfo getAssetsTypes();
    /**
     * @author SOFAS
     * @date   2020/5/27
     * @directions  添加新的资产
     * @return  com.bld.framework.web.domain.ResultInfo
     */
    ResultInfo addAsset(JSONObject json);
    ResultInfo updateAsset(JSONObject json);
    /**
     * @author SOFAS
     * @date   2020/5/27
     * @directions  删除资产
     * @param assetId  资产id
     * @return  com.bld.framework.web.domain.ResultInfo
     */
    ResultInfo delAsset(String assetId);
    /**
     * @author SOFAS
     * @date   2020/5/28
     * @directions  分配资产给指定客户
     * @param assetsId  资产id
     * @param customerId  客户id
     * @return  com.bld.framework.web.domain.ResultInfo
     */
    ResultInfo distributionAssets(String assetsId, String customerId);
    /**
     * @author SOFAS
     * @date   2020/5/28
     * @directions  删除资产分配
     * @param assetsId  资产id
     * @return  com.bld.framework.web.domain.ResultInfo
     */
    ResultInfo delDistribution(String assetsId);

    ResultInfo clientAssetsList(int limit, String search, String id);
    ResultInfo publicAsset(String assetId);
}
