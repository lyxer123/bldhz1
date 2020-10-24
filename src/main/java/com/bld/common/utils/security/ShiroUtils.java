package com.bld.common.utils.security;

import com.alibaba.fastjson.JSONObject;
import com.bld.framework.utils.OkHttpUtil;
import com.bld.project.system.user.domain.ThingsboardUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import com.bld.common.utils.StringUtils;
import com.bld.common.utils.bean.BeanUtils;
import com.bld.framework.shiro.realm.UserRealm;
import com.bld.project.system.user.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static com.bld.project.utils.TbApiUtils.getCurrentUserInfoApi;


/**
 * shiro 工具类
 *
 * @author bld
 */
@Component
public class ShiroUtils {
    @Value("${app.url}")
    public String url;
    public static String URL;

    @PostConstruct
    public void init() {
        URL = this.url;
    }


    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    public static Session getSession() {
        return SecurityUtils.getSubject().getSession();
    }

    public static void logout() {
        getSubject().logout();
    }

    public static User getSysUser() {
        User user = null;
        Object obj = getSubject().getPrincipal();
        if (StringUtils.isNotNull(obj)) {
            user = new User();
            BeanUtils.copyBeanProp(user, obj);
        }
        if (user == null) {
            return user;
        } else {
            String url = URL + "/api/auth/login";
            String userName = user.getUserName();
            String password = user.getPassword();
            //{"username":"1005750@qq.com", "password":"123654"}
            String jp = "{\"username\":" + "\"" + userName + "\"," + "\"password\":" + "\"" + password + "\"}";
            String s = OkHttpUtil.postJsonParams1(url, jp);
            JSONObject jsonObject = JSONObject.parseObject(s);
            if (jsonObject == null || jsonObject.getString("token") == null) {
                user = null;
            } else {
                String token = "Bearer " + jsonObject.getString("token");
                user.setAuthorization(token);
                /*获取thingsboard 用户信息*/
                if (user.getThingsboardUser() == null){
                    user.setThingsboardUser(JSONObject.parseObject(OkHttpUtil.get(getCurrentUserInfoApi(), null, token), ThingsboardUser.class));
                }
            }
            return user;
        }
    }

    public static void setSysUser(User user) {
        Subject subject = getSubject();
        PrincipalCollection principalCollection = subject.getPrincipals();
        String realmName = principalCollection.getRealmNames().iterator().next();
        PrincipalCollection newPrincipalCollection = new SimplePrincipalCollection(user, realmName);
        // 重新加载Principal
        subject.runAs(newPrincipalCollection);
    }

    public static String getTbToken() {
        return getSysUser().getAuthorization();
    }

    public static void clearCachedAuthorizationInfo() {
        RealmSecurityManager rsm = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        UserRealm realm = (UserRealm) rsm.getRealms().iterator().next();
        realm.clearCachedAuthorizationInfo();
    }

    public static Long getUserId() {
        return getSysUser().getUserId().longValue();
    }

    public static String getLoginName() {
        return getSysUser().getLoginName();
    }

    public static String getIp() {
        return getSubject().getSession().getHost();
    }

    public static String getSessionId() {
        return String.valueOf(getSubject().getSession().getId());
    }
}
