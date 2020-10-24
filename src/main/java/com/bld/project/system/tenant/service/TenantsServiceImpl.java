package com.bld.project.system.tenant.service;

import com.alibaba.fastjson.JSONObject;
import com.bld.common.utils.security.ShiroUtils;
import com.bld.framework.utils.OkHttpUtil;
import com.bld.framework.web.domain.ResultInfo;
import com.bld.project.utils.TbApiUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class TenantsServiceImpl implements TenantsService {
    @Override
    public ResultInfo getTenants(int limit, String search) {
        String url = TbApiUtils.getTenantListApi(limit, search);
        JSONObject json = TbApiUtils.get(url);
        return json == null ? ResultInfo.error("查询失败") : ResultInfo.success(json.get("data"));
    }

    @Override
    public ResultInfo addTenant(JSONObject json) {
        String url = TbApiUtils.addTenantApi();
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("X-Authorization", ShiroUtils.getTbToken());
        return OkHttpUtil.bldPostJsonParams(url, json.toJSONString(), headerMap);
    }

    @Override
    public ResultInfo delTenant(String tenantId) {
        String url = TbApiUtils.delTenantApi(tenantId);
        return OkHttpUtil.bldDeleteJsonParams(url, ShiroUtils.getTbToken());
    }

    @Override
    public ResultInfo updateTenant(JSONObject json) {
//        Object name = json.get("name");
//        Object title = json.get("title");
//        Object region = json.get("region");
//        Object phone = json.get("phone");
//        Object email = json.get("email");
//        Object country = json.get("country");
//        Object state = json.get("state");
//        Object city = json.get("city");
//        Object address = json.get("address");
//        Object address2 = json.get("address2");
//        Object zip = json.get("zip");
//        Object id = json.get("id");
//        Object createdTime = json.get("createdTime");
//        Object description = json.get("description");
        String url = TbApiUtils.updateTenantApi();
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("X-Authorization", ShiroUtils.getTbToken());
        return OkHttpUtil.bldPostJsonParams(url, json.toJSONString(), headerMap);
    }
}
