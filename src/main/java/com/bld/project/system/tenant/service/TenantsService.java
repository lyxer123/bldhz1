package com.bld.project.system.tenant.service;

import com.alibaba.fastjson.JSONObject;
import com.bld.framework.web.domain.ResultInfo;

/**
 * @author SOFAS
 * @date 2020/6/5
 * @directions  管理员处理接口
*/
public interface TenantsService {
    /**
     * @author SOFAS
     * @date   2020/6/5
     * @directions  查询租户列表
     * @param limit  查询数量
     * @param search  查询调教
     * @return  com.bld.framework.web.domain.ResultInfo
     */
    ResultInfo getTenants(int limit, String search);
    /**
     * @author SOFAS
     * @date   2020/6/5
     * @directions  添加租户
     * @param json  添加信息
     * @return  com.bld.framework.web.domain.ResultInfo
     */
    ResultInfo addTenant(JSONObject json);
    /**
     * @author SOFAS
     * @date   2020/6/5
     * @directions  删除指定租户
     * @param tenantId  租户id
     * @return  com.bld.framework.web.domain.ResultInfo
     */
    ResultInfo delTenant(String tenantId);
    /**
     * @author SOFAS
     * @date   2020/6/5
     * @directions  修改租户信息
     * @param json  修改后的信息
     * @return  com.bld.framework.web.domain.ResultInfo
     */
    ResultInfo updateTenant(JSONObject json);
}
