package com.bld.project.system.tenantAdmin;

import com.alibaba.fastjson.JSONObject;
import com.bld.common.utils.StringUtils;
import com.bld.framework.web.domain.ResultInfo;
import com.bld.project.system.tenantAdmin.service.TenantAdminService;
import com.bld.project.system.user.domain.ThingsboardUser;
import com.bld.project.system.user.service.IUserService;
import com.bld.project.utils.ListQuery;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
@RequestMapping("tenantAdmin")
public class TenantAdminController {
    @Resource
    private TenantAdminService tenantAdminService;
    @Resource
    private IUserService iUserService;

    @GetMapping("tenantAdmin.html")
    public String tenantAdmin(){
        return "system/cilent/tenantAdmin.html";
    }

    @RequestMapping("getTenantAdminList.json")
    @ResponseBody
    public ResultInfo getTenantAdminList(@RequestBody ListQuery query) {
        return tenantAdminService.getTenantAdminList(query.getId(), query.getLimit(), query.getSearch());
    }

    @PostMapping("addTenantAdmin.json")
    @ResponseBody
    public ResultInfo addTenantAdmin(@RequestBody ThingsboardUser tbUser){
        return iUserService.addUserInfo(tbUser);
    }

    @PostMapping("delTenantAdmin.json")
    @ResponseBody
    public ResultInfo delTenantAdmin(@RequestBody JSONObject json){
        Object tbUserId = json.get("tbUserId");
        if (StringUtils.isNullString(tbUserId)){
            return ResultInfo.error("userId不能为空");
        }
        return iUserService.deleteUserInfo(tbUserId.toString());
    }

    @PostMapping("updateTenantAdmin.json")
    @ResponseBody
    public ResultInfo updateTenantAdmin(@RequestBody JSONObject json){
        return tenantAdminService.updateTenantAdmin(json);
    }

    @RequestMapping("loginUser.json")
    @ResponseBody
    public ResultInfo loginUser(@RequestBody JSONObject json){
        Object loginUserId = json.get("loginUserId");
        if (StringUtils.isNullString(loginUserId)){
            return ResultInfo.error("没有获取到用户id");
        }
        return iUserService.loginUser(loginUserId.toString());
    }
}
