package com.bld.project.system.assets.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bld.common.utils.security.ShiroUtils;
import com.bld.framework.utils.OkHttpUtil;
import com.bld.framework.web.domain.ResultInfo;
import com.bld.project.utils.TbApiUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class AssetsServiceImpl implements AssetsService {

    @Override
    public ResultInfo assetsList(int limit, String search) {
        String url = TbApiUtils.assetsListApi(limit, search);
        ResultInfo<String> resultInfo = TbApiUtils.bldGet(url);
        if (resultInfo.isSuccess()){
            JSONObject data = JSONObject.parseObject(resultInfo.getData());
            List list = JSONObject.parseObject(data.get("data").toString(), List.class);
            return ResultInfo.success(list, resultInfo.getMessage());
        }
        return resultInfo;
    }

    @Override
    public ResultInfo getAssetsTypes() {
        String url = TbApiUtils.getAssetsTypesApi();
        List list = TbApiUtils.getList(url, null);
        ArrayList<String> list1 = new ArrayList<>();
        for (Object o : list){
            JSONObject json = JSONObject.parseObject(o.toString());
            if (json != null){
                list1.add(json.get("type").toString());
            }
        }
        return ResultInfo.success(list1);
    }

    @Override
    public ResultInfo addAsset(JSONObject json) {
        String url = TbApiUtils.addAssetApi();
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("X-Authorization", ShiroUtils.getTbToken());
        return OkHttpUtil.bldPostJsonParams(url, json.toJSONString(), headerMap);
    }

    @Override
    public ResultInfo updateAsset(JSONObject json) {
        String url = TbApiUtils.addAssetApi();
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("X-Authorization", ShiroUtils.getTbToken());
        return OkHttpUtil.bldPostJsonParams(url, json.toJSONString(), headerMap);
    }

    @Override
    public ResultInfo delAsset(String assetId) {
        String url = TbApiUtils.delAssetApi(assetId);
        return OkHttpUtil.bldDeleteJsonParams(url, ShiroUtils.getTbToken());
    }

    @Override
    public ResultInfo distributionAssets(String assetsId, String customerId) {
        String url = TbApiUtils.distributionAssets(assetsId, customerId);
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("X-Authorization", ShiroUtils.getTbToken());
        return OkHttpUtil.bldPostJsonParams(url, headerMap);
    }

    @Override
    public ResultInfo delDistribution(String assetsId) {
        String url = TbApiUtils.delDistribution(assetsId);
        return OkHttpUtil.bldDeleteJsonParams(url, ShiroUtils.getTbToken());
    }

    @Override
    public ResultInfo clientAssetsList(int limit, String search, String id) {
        String url = TbApiUtils.clientAssetsList(limit, search, id);
        ResultInfo<String> resultInfo = TbApiUtils.bldGet(url);
        if (resultInfo.isSuccess()){
            String data = resultInfo.getData();
            JSONObject json = JSONObject.parseObject(data);
            List<JSONObject> list = JSONObject.parseArray(json.getString("data"), JSONObject.class);
            for (JSONObject j1 : list){
                String ids = j1.getString("id");
                JSONObject j2 = JSONObject.parseObject(ids, JSONObject.class);
                j1.put("id", j2.get("id"));
            }
            return ResultInfo.success(list);
        }
        return resultInfo;
    }

    @Override
    public ResultInfo publicAsset(String assetId) {
        String url = TbApiUtils.publicAssetApi(assetId);
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("X-Authorization", ShiroUtils.getTbToken());
        return OkHttpUtil.bldPostJsonParams(url, headerMap);
    }
}
