package com.bld.project.system.tenantAdmin.service;

import com.alibaba.fastjson.JSONObject;
import com.bld.framework.web.domain.ResultInfo;

public interface TenantAdminService {
    /**
     * @author SOFAS
     * @date   2020/6/5
     * @directions  查询租户管理员列表
     * @param tenantId  租户id
     * @param limit  查询数量
     * @param search  查询条件
     * @return  java.lang.String
     */
    ResultInfo getTenantAdminList(String tenantId, int limit, String search);
    /**
     * @author SOFAS
     * @date   2020/6/5
     * @directions  修改租户管理员信息
     * @param json  修改的信息
     * @return  com.bld.framework.web.domain.ResultInfo
     */
    ResultInfo updateTenantAdmin(JSONObject json);
}
