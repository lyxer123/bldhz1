package com.bld.project.system.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.bld.common.constant.ShiroConstants;
import com.bld.common.utils.DateUtils;
import com.bld.common.utils.ServletUtils;
import com.bld.common.utils.StringUtils;
import com.bld.common.utils.security.ShiroUtils;
import com.bld.framework.web.controller.BaseController;
import com.bld.framework.web.domain.ResultInfo;
import com.bld.project.system.user.domain.ThingsboardUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Date;

/**
 * 登录验证
 *
 * @author bld
 */
@Slf4j
@Controller
public class LoginController extends BaseController {
    @Value("${app.url}")
    private String URL;

    @GetMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response) {
        // 如果是Ajax请求，返回Json字符串。
        if (ServletUtils.isAjaxRequest(request)) {
            return ServletUtils.renderString(response, "{\"code\":\"1\",\"msg\":\"未登录或登录超时。请重新登录\"}");
        }
        return "/static/index.html";
    }

    @CrossOrigin
    @PostMapping("/login")
    @ResponseBody
    public ResultInfo ajaxLogin(@RequestBody JSONObject json) {
        Object userName = json.get("username");
        Object password = json.get("password");
//        Object captcha = json.get("captcha");
        if (StringUtils.isNullString(userName)){
            return ResultInfo.error("请输入用户名");
        }
        if (StringUtils.isNullString(password)){
            return ResultInfo.error("请输入密码");
        }
        ServletUtils.getRequest().setAttribute(ShiroConstants.CURRENT_CAPTCHA, null);
        long nowTime = System.currentTimeMillis();
        Session session = ShiroUtils.getSession();
        int errorNum = 0;
        Object errorNumO = session.getAttribute("errorNum");
        if (errorNumO != null){
            errorNum = Integer.valueOf(errorNumO.toString());
            Object errorTimeO = session.getAttribute("errorTime");
            if (errorTimeO != null){
                long errorTime = Long.valueOf(errorTimeO.toString());
                long afterTime = DateUtils.getAfterTimestamp(new Date(errorTime), 1, Calendar.HOUR);
                if (errorNum > 5 && afterTime > nowTime){
                    return ResultInfo.error("您错误次数过多，请一小时后再登陆");
                }
            }
        }
        UsernamePasswordToken token = new UsernamePasswordToken(userName.toString(), password.toString(), true);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            if (ShiroUtils.getSysUser() == null) {
                subject.logout();
                Session session1 = ShiroUtils.getSession();
                session1.setAttribute("errorNum",  errorNum + 1);
                session1.setAttribute("errorTime",  nowTime);
                return ResultInfo.error("用户或密码错误");
            }
            ThingsboardUser tbUser = ShiroUtils.getSysUser().getThingsboardUser();
            if (!"4ad271a0-bf61-11ea-aab8-4bc49e65094a".equals(tbUser.getCustomerId().getId()) && !tbUser.isTenantAdmin()){
                subject.logout();
                Session session1 = ShiroUtils.getSession();
                session1.setAttribute("errorNum",  errorNum + 1);
                session1.setAttribute("errorTime",  nowTime);
                return ResultInfo.error("您没有权限登陆该账户");
            }
            session.setAttribute("errorNum",  0);
            session.setAttribute("errorTime",  null);
            Object token1 = ShiroUtils.getTbToken();
            return ResultInfo.success(token1, "登陆成功");
        } catch (NullPointerException e) {
            session.setAttribute("errorNum",  errorNum + 1);
            session.setAttribute("errorTime",  nowTime);
            return ResultInfo.error("用户或密码错误");
        }
    }

    @GetMapping("/unauth")
    public String unauth() {
        return "error/unauth";
    }
}
