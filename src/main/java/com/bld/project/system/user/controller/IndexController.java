package com.bld.project.system.user.controller;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bld.framework.utils.OkHttpUtil;
import com.bld.project.system.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import com.bld.framework.config.BldConfig;
import com.bld.framework.web.controller.BaseController;
import com.bld.project.system.menu.service.IMenuService;
import com.bld.project.system.user.domain.User;

import javax.annotation.Resource;

/**
 * 首页 业务处理
 *
 * @author bld
 */
@Controller
public class IndexController extends BaseController {
    @Value("${app.url}")
    private String URL;

    @Autowired
    private BldConfig bldConfig;

    // 系统首页
    @GetMapping("/index")
    public String index() {
        return "/static/index.html";
    }

    // 切换主题
    @GetMapping("/system/switchSkin")
    public String switchSkin(ModelMap mmap) {
        return "skin";
    }

    // 系统介绍
    @GetMapping("/system/main")
    public String main(ModelMap mmap) {
        User user = getSysUser();
        String token = user.getAuthorization();
        String url1 = URL + "/api/tenant/devices";
        Map<String, String> map11 = new HashMap<>();
        map11.put("limit", "10000");
        String s1 = OkHttpUtil.get(url1, map11, token);
        JSONObject object = JSON.parseObject(s1);
        if (object != null) {
            String data = object.getString("data");
            JSONArray result = JSONObject.parseArray(data);
            int size = result.size();
            mmap.put("size", size);
        }
        mmap.put("size", 0);
        mmap.put("version", bldConfig.getVersion());
        return "main";
    }


}
