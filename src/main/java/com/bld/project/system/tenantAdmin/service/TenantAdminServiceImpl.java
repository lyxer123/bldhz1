package com.bld.project.system.tenantAdmin.service;

import com.alibaba.fastjson.JSONObject;
import com.bld.common.utils.StringUtils;
import com.bld.common.utils.security.ShiroUtils;
import com.bld.framework.utils.OkHttpUtil;
import com.bld.framework.web.domain.ResultInfo;
import com.bld.project.utils.TbApiUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;

import static com.bld.project.utils.TbApiUtils.updateUserInfoApi;

@Service
public class TenantAdminServiceImpl implements TenantAdminService{
    @Override
    public ResultInfo getTenantAdminList(String tenantId, int limit, String search) {
        String url = TbApiUtils.getTenantAdminListApi(tenantId, limit, search);
        JSONObject json = TbApiUtils.get(url);
        return json == null ? ResultInfo.error("查询失败") : ResultInfo.success(json.get("data"));
    }

    @Override
    public ResultInfo updateTenantAdmin(JSONObject json) {
        Object email = json.get("email");
        if (StringUtils.isNullString(email)){
            return ResultInfo.error("没有获取到邮箱");
        }
        Object id = json.get("id");
        Object tenantId = json.get("tenantId");
        Object customerId = json.get("customerId");
        if (StringUtils.isNullString(id) || StringUtils.isNullString(tenantId) || StringUtils.isNullString(customerId)){
            return ResultInfo.error("参数错误");
        }
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("X-Authorization", ShiroUtils.getTbToken());
        return OkHttpUtil.bldPostJsonParams(updateUserInfoApi(), json.toJSONString(), headerMap);
    }


}
