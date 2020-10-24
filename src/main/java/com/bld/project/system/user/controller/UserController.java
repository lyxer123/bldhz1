package com.bld.project.system.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.bld.common.utils.StringUtils;
import com.bld.common.utils.security.ShiroUtils;
import com.bld.framework.web.controller.BaseController;
import com.bld.framework.web.domain.ResultInfo;
import com.bld.project.system.user.domain.ThingsboardUser;
import com.bld.project.system.user.service.IUserService;
import com.bld.project.utils.ListQuery;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户信息
 * 
 * @author bld
 */
@Controller
@RequestMapping("/system/user")
public class UserController extends BaseController {
    @Resource
    private IUserService iUserService;

    @GetMapping("user.html")
    public String openUserManage(){
        return "system/cilent/user.html";
    }

    @RequestMapping("/getCurrentUserInfo.json")
    @ResponseBody
    public ResultInfo getCurrentUserInfo(){
        return iUserService.getCurrentUserInfo();
    }

    @RequestMapping("/updateUserInfo.json")
    @ResponseBody
    public ResultInfo updateUserInfo(@RequestBody JSONObject json){
        if (StringUtils.isNullString(json.get("email"))){
            return ResultInfo.error("没有获取到邮箱");
        }
        return iUserService.updateUserInfo(json);
    }

    @RequestMapping("/addUserInfo.json")
    @ResponseBody
    public ResultInfo addUserInfo(@RequestBody String json){
        ThingsboardUser tbUser = JSONObject.parseObject(json, ThingsboardUser.class);
        return iUserService.addUserInfo(tbUser);
    }

    @RequestMapping("/deleteUserInfo.json")
    @ResponseBody
    public ResultInfo deleteUserInfo(@RequestBody JSONObject json){
        Object tbUserId = json.get("tbUserId");
        if (StringUtils.isNullString(tbUserId)){
            return ResultInfo.error("userId不能为空");
        }
        return iUserService.deleteUserInfo(tbUserId.toString());
    }

    @PostMapping("/modifyPassword.json")
    @ResponseBody
    public ResultInfo modifyPassword(@RequestBody JSONObject json){
        String newPassword = json.getString("newPassword");
        String oldPassword = json.getString("oldPassword");
        if (StringUtils.isNullString(newPassword) || StringUtils.isNullString(oldPassword)){
            return ResultInfo.error("参数错误");
        }
        if (newPassword.equals(oldPassword)){
            return ResultInfo.error("新密码不能与旧密码相同");
        }
        return iUserService.modifyPassword(newPassword, oldPassword);
    }
    @RequestMapping("/getUserActivationUrl.json")
    @ResponseBody
    public ResultInfo getUserActivationUrl(String tbUserId){
        return iUserService.getUserActivationUrl(tbUserId);
    }

    @RequestMapping("/getUserListByCustomerId.json")
    @ResponseBody
    public ResultInfo getUserListByCustomerId(@RequestBody ListQuery query){
        return iUserService.getUserListByCustomerId(query.getLimit(), query.getSearch(), query.getId());
    }

    @PostMapping("modifyUserStatus.json")
    @ResponseBody
    public ResultInfo modifyUserStatus(@RequestBody JSONObject json){
        Object userId = json.get("userId");
        if (StringUtils.isNullString(userId)){
            return ResultInfo.error("没有获取到您要修改的用户ID");
        }
        Object status = json.get("status");
        if (StringUtils.isNullString(status)){
            return ResultInfo.error("没有获取到您要修改的用户状态");
        }
        return iUserService.modifyUserStatus(userId.toString(), Boolean.valueOf(status.toString()));
    }

    @RequestMapping("getUrl.json")
    @ResponseBody
    public ResultInfo geturl(String userId){
        if (StringUtils.isNullString(userId)){
            return ResultInfo.error("没有获取到用户ID");
        }
        return iUserService.getUrl(userId);
    }

    @GetMapping("logout.json")
    @ResponseBody
    public ResultInfo getDeviceTypes(){
        Subject subject = ShiroUtils.getSubject();
        if (subject != null) {
            subject.logout();
        }
        return ResultInfo.success("退出登录");
    }
}