package com.bld.project.system.user.controller;

import com.bld.framework.utils.OkHttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bld.common.utils.StringUtils;
import com.bld.framework.aspectj.lang.annotation.Log;
import com.bld.framework.aspectj.lang.enums.BusinessType;
import com.bld.framework.shiro.service.PasswordService;
import com.bld.framework.web.controller.BaseController;
import com.bld.framework.web.domain.AjaxResult;
import com.bld.project.system.user.domain.User;
import com.bld.project.system.user.service.IUserService;


/**
 * 个人信息 业务处理
 * 
 * @author bld
 */
@Controller
@RequestMapping("/system/user/profile")
public class ProfileController extends BaseController
{
    private static final Logger log = LoggerFactory.getLogger(ProfileController.class);

    private String prefix = "system/user/profile";

    @Value("${app.url}")
    private static String URL;

    @Autowired
    private IUserService userService;

    @Autowired
    private PasswordService passwordService;

    /**
     * 个人信息
     */
    @GetMapping()
    public String profile(ModelMap mmap)
    {
        User user = getSysUser();
        mmap.put("user", user);
        mmap.put("roleGroup", userService.selectUserRoleGroup(user.getUserId()));
        mmap.put("postGroup", userService.selectUserPostGroup(user.getUserId()));
        return prefix + "/profile";
    }

    @GetMapping("/checkPassword")
    @ResponseBody
    public boolean checkPassword(String password)
    {
        User user = getSysUser();
        if (passwordService.matches(user, password))
        {
            return true;
        }
        return false;
    }

    @GetMapping("/resetPwd")
    public String resetPwd(ModelMap mmap)
    {
        User user = getSysUser();
        mmap.put("user", user);
        return prefix + "/resetPwd";
    }

    @Log(title = "重置密码", businessType = BusinessType.UPDATE)
    @PostMapping("/resetPwd")
    @ResponseBody
    public AjaxResult resetPwd(String oldPassword, String newPassword)
    {
        User user = getSysUser();
        String jsonParams="{\"username\":\""+user.getUserName()+"\"," +"\"password\":\""+user.getPassword()+"\"}";
        if (StringUtils.isNotEmpty(newPassword) && passwordService.matches(user, oldPassword)){
            String token=user.getAuthorization();
            String url1=URL+"/api/auth/changePassword";
            String json="{\"currentPassword\":\""+oldPassword+"\",\"newPassword\":\""+newPassword+"\"}";
            OkHttpUtil.postJsonParams(url1,json,token);

            return success();

        } else
        {
            return error("修改密码失败");
        }

    }

    /**
     * 修改用户
     */
    @GetMapping("/edit")
    public String edit(ModelMap mmap)
    {
        User user = getSysUser();
        mmap.put("user", userService.selectUserById(user.getUserId()));
        return prefix + "/edit";
    }

    /**
     * 修改头像
     */
    @GetMapping("/avatar")
    public String avatar(ModelMap mmap)
    {
        User user = getSysUser();
        mmap.put("user", userService.selectUserById(user.getUserId()));
        return prefix + "/avatar";
    }
}
