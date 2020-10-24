package com.bld.project.system.role.controller;

import com.alibaba.fastjson.JSONObject;
import com.bld.framework.utils.OkHttpUtil;
import com.bld.framework.web.controller.BaseController;
import com.bld.framework.web.page.TableDataInfo;
import com.bld.project.system.post.service.IPostService;
import com.bld.project.system.role.service.IRoleService;
import com.bld.project.system.user.domain.Client;
import com.bld.project.system.user.domain.User;
import com.bld.project.system.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.bld.common.utils.security.ShiroUtils.getSysUser;

@Controller
@RequestMapping("/system/client")
public class ClientController  extends BaseController {

    private String prefix = "system/cilent";

    @Value("${app.url}")
    private String URL;

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IPostService postService;

    @GetMapping()
    public String client()
    {
        return  prefix+ "/cilent";
    }



    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo customers(Client client1,ModelMap modelMap){
        User sysUser = getSysUser();
        String authorization = sysUser.getAuthorization();
        String url=URL+"/api/customers?limit=10000";
        String s = OkHttpUtil.get(url, null, authorization);
        JSONObject parse = (JSONObject) JSONObject.parse(s);
        String data = parse.getString("data");
        List<Client> clients=new ArrayList<>();
        List<Map> maps1 = JSONObject.parseArray(data, Map.class);
        //List<Map> maps = customers.toJavaList(Map.class);
        for (Map map : maps1) {
            Client client=new Client();
            String id = ((JSONObject) map.get("id")).getString("id");
            String name= String.valueOf(map.get("name"));
            String adress=String.valueOf(map.get("adress"));
            String phone=String.valueOf(map.get("phone"));
            client.setId(id);
            client.setName(name);
            client.setAdress(adress);
            client.setPhone(phone);
            clients.add(client);
        }
        TableDataInfo dataTable = getDataTable(clients);
        return dataTable;
    }
}
