package com.bld.project.system.tenant;

import com.alibaba.fastjson.JSONObject;
import com.bld.common.utils.StringUtils;
import com.bld.framework.web.domain.ResultInfo;
import com.bld.project.system.tenant.service.TenantsService;
import com.bld.project.utils.ListQuery;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author SOFAS
 * @date 2020/6/5
 * @directions  系统管理员请求处理
*/
@Controller
@RequestMapping("tenant")
public class TenantsController {
    @Resource
    private TenantsService tenantsService;

    @GetMapping("tenant.html")
    public String tenant(){
        return "system/cilent/tenant.html";
    }

    @RequestMapping("getTenantList.json")
    @ResponseBody
    public ResultInfo getTenants(@RequestBody ListQuery query){
        return tenantsService.getTenants(query.getLimit(), query.getSearch());
    }

    @PostMapping("addTenant.json")
    @ResponseBody
    public ResultInfo addTenant(@RequestBody JSONObject json){
        return tenantsService.addTenant(json);
    }

    @PostMapping("delTenant.json")
    @ResponseBody
    public ResultInfo delTenant(@RequestBody JSONObject json){
        Object tenantId = json.get("tenantId");
        if (StringUtils.isNullString(tenantId)){
            return ResultInfo.error("没有获取到您要删除的租户id");
        }
        return tenantsService.delTenant(tenantId.toString());
    }

    @PostMapping("updateTenant.json")
    @ResponseBody
    public ResultInfo updateTenant(@RequestBody JSONObject json){
        Object name = json.get("name");
        Object title = json.get("title");
        if (StringUtils.isNullString(name) && StringUtils.isNullString(title)){
            return ResultInfo.error("没有获取到租户名称");
        }
        return tenantsService.updateTenant(json);
    }
}
