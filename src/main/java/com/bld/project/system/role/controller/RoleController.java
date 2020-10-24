package com.bld.project.system.role.controller;

import ch.ethz.ssh2.Connection;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bld.common.utils.StringUtils;
import com.bld.common.utils.poi.ExcelUtil;
import com.bld.framework.aspectj.lang.annotation.Log;
import com.bld.framework.aspectj.lang.enums.BusinessType;
import com.bld.framework.config.ConnConfig;
import com.bld.framework.utils.Hex;
import com.bld.framework.utils.OkHttpUtil;
import com.bld.framework.web.controller.BaseController;
import com.bld.framework.web.domain.AjaxResult;
import com.bld.framework.web.domain.ResultInfo;
import com.bld.framework.web.domain.Results;
import com.bld.framework.web.page.TableDataInfo;
import com.bld.project.system.block.mapper.BlockDeviceMapper;
import com.bld.project.system.block.mapper.BlockHashMapper;
import com.bld.project.system.block.model.Block;
import com.bld.project.system.block.model.BlockDevice;
import com.bld.project.system.block.model.BlockHash;
import com.bld.project.system.device.service.DeviceService;
import com.bld.project.system.role.domain.*;
import com.bld.project.system.role.service.IRoleService;
import com.bld.project.system.user.domain.User;
import com.bld.project.system.user.domain.UserRole;
import com.bld.project.system.user.service.IUserService;
import com.bld.project.utils.BlockUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.web3j.crypto.CipherException;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 角色信息
 *
 * @author bld
 */
@Slf4j
@Controller
@RequestMapping("/system/role")
public class RoleController extends BaseController {
    @Value("${linux.ip}")
    private String ip;

    @Value("${linux.port}")
    private int port;

    @Value("${linux.user}")
    private String user;

    @Value("${linux.password}")
    private String password;

    private String prefix = "system/role";

    @Value("${app.url}")
    private String url;

    @Value("${block.peerUrl1}")
    private String peerUrl1;

    @Value("${block.balance}")
    private String balance;

    //自动新增用户名
    @Value("${tenat.userName}")
    private String auotoAddUserName;

    @Value("${tenat.password}")
    private String auotoAddPassword;

    @Value("${tenat.check}")
    private String check;

    @Value("${block.password}")
    private String walletPassword;


    @Autowired
    private IRoleService roleService;

    @Autowired
    private IUserService userService;
    @Resource
    private BlockDeviceMapper blockDeviceMapper;
    @Resource
    private BlockHashMapper blockHashMapper;


    @GetMapping()
    public String role() {
        return prefix + "/role";
    }

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list() throws Exception {
        User user = getSysUser();
        startPage();
        List<Pileasset> pileassets = roleService.listPileasset();
        for (Pileasset pileasset : pileassets) {
            if (pileasset.getFromWallet() == null || "".equals(pileasset.getFromWallet())) {
                pileasset.setMoney("0");
            } else {
                //获取余额
                String jp = "{\"jsonrpc\":\"2.0\",\"method\":\"eth_getBalance\",\"params\":[\"" + pileasset.getFromWallet() + "\", \"latest\"],\"id\":1}";
                String s5 = OkHttpUtil.postJsonParams1(peerUrl1, jp);
                JSONObject jsonObject8 = JSONObject.parseObject(s5);
                String result1 = jsonObject8.getString("result");
                String substring = result1.substring(2, result1.length());
                BigInteger big = new BigInteger(substring.trim(), 16);
                String money = big.toString();
                pileasset.setMoney(money);
            }
            Random random = new Random();
            double v = random.nextDouble() * (5.9 - 5.1) + 5.1;
            double ua = random.nextDouble() * (220 - 215) + 220;
            double pa = v * ua;
            BigDecimal bigDecimal = new BigDecimal(v).setScale(2, RoundingMode.HALF_UP);
            BigDecimal bigDecimal2 = new BigDecimal(ua).setScale(2, RoundingMode.HALF_UP);
            BigDecimal bigDecimal3 = new BigDecimal(pa).setScale(2, RoundingMode.HALF_UP);
            double v1 = bigDecimal.doubleValue();
            double v2 = bigDecimal2.doubleValue();
            double v3 = bigDecimal3.doubleValue();
            //*System.out.println(v);*//*
            String blockData = "{\"UA\":" + "\"" + v2 + "\"," + "\"IA\":" + "\"" + v1 + "\"," + "\"PA\":" + "\"" + v3 + "\"" + "}";
            //System.out.println(accessToken);
            pileasset.setBlockData(blockData);
        }

        //获取账户权限验证码


        //List<Role> list1 = roleService.selectRoleList(role);

/*
        PageInfo<Device> pageInfo=new PageInfo(pileassets,pagesize);
*/
        TableDataInfo dataTable = getDataTable(pileassets);
        // System.out.println(dataTable.toString());
        return dataTable;
    }

    @Log(title = "角色管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Role role) {
        List<Role> list = roleService.selectRoleList(role);
        ExcelUtil<Role> util = new ExcelUtil<Role>(Role.class);
        return util.exportExcel(list, "角色数据");
    }

    /**
     * 新增角色
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    @PostMapping("/add")
    @ResponseBody
    public AjaxResult saveDevice(Pileasset pileasset) {
        User user = getSysUser();
        //请求物联网平台添加设备
        String token = user.getAuthorization();
        String url1 = url + "/api/device";
        String chipId = pileasset.getChipId();
        String type = pileasset.getType();
        String info = "{\"name\":\"" + chipId + "\",\"type\":\"" + type + "\"}";
        try {
            //请求物联网添加设备
            String s1 = OkHttpUtil.postJsonParams(url1, info, token);
            //获取设备id
            JSONObject jsonId = (JSONObject) JSONObject.parseObject(s1).get("id");
            String deviceId = jsonId.getString("id");
            pileasset.setDeviceId(deviceId);
            //获取物联网设备token
            String url4 = url + "/api/device/" + deviceId + "/credentials";
            String s2 = OkHttpUtil.get(url4, null, user.getAuthorization());
            //System.out.println(s2);
            JSONObject jsonObject5 = JSONObject.parseObject(s2);
            String accessToken = jsonObject5.getString("credentialsId");
            pileasset.setToken(accessToken);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return error("该设备已存在");
        }

        String json5 = "{\"method\":\"personal_newAccount\",\"params\":[\"" + walletPassword + "\"], \"id\":1,\"jsonrpc\":\"2.0\"}";
        String coinbaseJson = "{\"jsonrpc\":\"2.0\",\"method\":\"eth_coinbase\",\"params\":[],\"id\":64}";
        int save = roleService.save(pileasset);
        Pileasset pileasset1 = roleService.pileassetbyChipId(pileasset.getChipId());
        Integer id = pileasset1.getId();
        //判断奇偶
        if (id % 2 == 0) {
            String s4 = OkHttpUtil.postJsonParams1(peerUrl1, json5);
            String coinbaseStr = OkHttpUtil.postJsonParams1(peerUrl1, coinbaseJson);
            String wallent = JSONObject.parseObject(s4).getString("result");
            String coinbase = JSONObject.parseObject(coinbaseStr).getString("result");
            pileasset1.setFromWallet(wallent);
            pileasset1.setPeerUrl(peerUrl1);
            pileasset1.setToWallet(coinbase);
        } else {
            String s4 = OkHttpUtil.postJsonParams1(peerUrl1, json5);
            String coinbaseStr = OkHttpUtil.postJsonParams1(peerUrl1, coinbaseJson);
            String wallent = JSONObject.parseObject(s4).getString("result");
            String coinbase = JSONObject.parseObject(coinbaseStr).getString("result");
            pileasset1.setFromWallet(wallent);
            pileasset1.setPeerUrl(peerUrl1);
            pileasset1.setToWallet(coinbase);
        }
        int i = roleService.updatePileasset(pileasset1);
        return toAjax(save);
    }


    /**
     * 查看要修改的设备信息
     */
    @GetMapping("/edit/{chipId}")
    public String edit(@PathVariable("chipId") String chipId, ModelMap mmap) {

        Pileasset pileasset = roleService.pileassetbyChipId(chipId);
        mmap.put("pileasset", pileasset);
        return prefix + "/edit";
    }

    @PostMapping("/statusAdd")
    @ResponseBody
    public Pilestatus statusAdd(@RequestBody Pilestatus pilestatus) {
        int i = roleService.statusAdd(pilestatus);
        return pilestatus;
    }

    //获取遥测值
    @GetMapping("/telemetry/{deviceId}")
    public String telemetry(@PathVariable("deviceId") String deviceId, ModelMap mmap) {
        //登录
        String deviceType = "DEVICE";
        User user = getSysUser();
        String token = user.getAuthorization();
        //获取遥测的键集合
        String url1 = url + "/api/plugins/telemetry/" + deviceType + "/" + deviceId + "/keys/timeseries";
        String s1 = OkHttpUtil.get(url1, null, token);
        List<String> list = new ArrayList<>();
        String keys = "";
        if ("[]".equals(s1)) {
            s1 = "";
        } else {
            JSONArray objects = JSONArray.parseArray(s1);
            for (Object object : objects) {
                list.add((String) object);
            }
            keys = list.toString().replace("[", "").replace("]", "").replace(" ", "");
        }

        if (keys.isEmpty()) {
            mmap.put("msg", "没有遥测键");
            return prefix + "/error";
        }
        String url2 = url + "/api/plugins/telemetry/" + deviceType + "/" + deviceId + "/values/timeseries";
        Map<String, String> map1 = new HashMap<>();
        String startTs = "1577892385000";
        map1.put("keys", keys);
        map1.put("startTs", startTs);
        String endTs = System.currentTimeMillis() + "";
        map1.put("endTs", endTs);

        String s2 = OkHttpUtil.get(url2, map1, token);
        mmap.put("list", list);
        Map<String, JSONArray> map3 = new HashMap<>();
        /* Map<String, JSONArray> map4=new HashMap<>()*/
        ;
        JSONObject jsonObject1 = JSONObject.parseObject(s2);
        for (int i = 0; i < list.size(); i++) {
            map3.put(list.get(i), jsonObject1.getJSONArray(list.get(i)));
        }
        List<Telemetry> telemetryList = new ArrayList<>();
        for (Map.Entry<String, JSONArray> stringJSONArrayEntry : map3.entrySet()) {
            System.out.println(stringJSONArrayEntry.getKey() + stringJSONArrayEntry.getValue());
            List<Map> arrayLists = stringJSONArrayEntry.getValue().toJavaList(Map.class);
            List<Map> arrayLists1 = new ArrayList<>();
            //判断集合是否大于100判断
            if (0 < arrayLists.size() && arrayLists.size() < 100) {

                for (int i = 0; i < arrayLists.size(); i++) {
                    Telemetry telemetry = new Telemetry();
                    telemetry.setKey(stringJSONArrayEntry.getKey());
                    //获取时间戳
                    Long ts = (Long) arrayLists.get(i).get("ts");
                    //时间戳转换成类似2020-02-03 08:20:59格式
                    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(ts);
                    String format = sf.format(date);
                    //遥测telemetry实体类赋值
                    telemetry.setTs(format);
                    telemetry.setValue((String) arrayLists.get(i).get("value"));
                    telemetryList.add(telemetry);
                }
            } else if (arrayLists.size() >= 100) {
                for (int i = arrayLists.size() - 100; i < arrayLists.size() - 1; i++) {
                    Map map2 = arrayLists.get(i);
                    arrayLists1.add(i, map2);
                }
                for (int i = 0; i < arrayLists1.size(); i++) {
                    Telemetry telemetry = new Telemetry();
                    telemetry.setKey(stringJSONArrayEntry.getKey());
                    //获取时间戳
                    Long ts = (Long) arrayLists1.get(i).get("ts");
                    //时间戳转换成类似2020-02-03 08:20:59格式
                    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(ts);
                    String format = sf.format(date);
                    //遥测telemetry实体类赋值
                    telemetry.setTs(format);
                    telemetry.setValue((String) arrayLists1.get(i).get("value"));
                    telemetryList.add(telemetry);
                }
                mmap.put("telemetrys", telemetryList);
            }
        }
        return prefix + "/telemetry";
    }

    @PostMapping("/autoAdd")
    @ResponseBody
    public AjaxResult autoAdd(@RequestBody String info) {
        String url1 = url + "/api/auth/login";
        String json = "";
        JSONObject jsonObject1 = JSONObject.parseObject(info);
        String userName = jsonObject1.getString("userName");
        String password = jsonObject1.getString("password");
        String name = jsonObject1.getString("name");
        String type = jsonObject1.getString("type");
        json = "{\"name\":" + "\"" + name + "\",\"type\":" + "\"" + type + "\"}";
        String jsonParams = "{\"username\":\"" + userName + "\"," + "\"password\":\"" + password + "\"}";
        String s = OkHttpUtil.postJsonParams(url1, jsonParams, "");
        System.out.println(s);
        JSONObject jsonObject = JSONObject.parseObject(s);
        Map<String, Object> map = jsonObject;
        String token = check + (String) map.get("token");
        String url2 = url + "/api/device";
        String s1 = OkHttpUtil.postJsonParams(url2, json, token);
        return toAjax(1);
    }

    /**
     * 修改设备信息
     *
     * @param pileasset
     * @return
     */

    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Pileasset pileasset) {
        String deviceId = pileasset.getDeviceId();
        User user = getSysUser();
        String token = user.getAuthorization();
        String url1 = url + "/api/device/" + deviceId;
        String s1 = OkHttpUtil.get(url1, null, token);
        JSONObject jsonObject1 = JSONObject.parseObject(s1);
        Map<String, String> map1 = JSON.parseObject(jsonObject1.toJSONString(), Map.class);
        map1.put("name", pileasset.getChipId());
        String s2 = JSON.toJSONString(map1);
        System.out.println(s2);
        String url2 = url + "/api/device";
        OkHttpUtil.postJsonParams(url2, s2, token);
        int i = roleService.editPileasset(pileasset);
        return toAjax(i);
    }

    @GetMapping("/option")
    @ResponseBody
    public List<Ota> options() {
        List<Ota> otas = roleService.versionAll();
        return otas;
    }

    /**
     * 凭据信息
     */
    @GetMapping("/authDataScope/{deviceId}")
    public String authDataScope(@PathVariable("deviceId") String deviceId, ModelMap mmap) {
        Device device = new Device();
        User user = getSysUser();
        //获取访问令牌
        String token = user.getAuthorization();
        String url2 = url + "/api/device/" + deviceId + "/credentials";
        String s2 = OkHttpUtil.get(url2, null, token);
        JSONObject jsonObject5 = JSONObject.parseObject(s2);
        String accessToken = jsonObject5.getString("credentialsId");
        String credentialsType = jsonObject5.getString("credentialsType");
        System.out.println(accessToken);
        device.setDeviceId(deviceId);
        device.setToken(accessToken);
        device.setTokenType(credentialsType);
        mmap.put("role", device);

        //mmap.put("role", roleService.selectRoleById(roleId));
        return prefix + "/dataScope";
    }

    /**
     * 保存访问令牌
     */
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PostMapping("/authDataScope")
    @ResponseBody
    public AjaxResult authDataScopeSave(Device device) {
        //获取设备属性
        String deviceId = device.getDeviceId();
        String tokenType = device.getTokenType();
        String token1 = device.getToken();
        User user = getSysUser();
        String token = user.getAuthorization();
        String url2 = url + "/api/device/" + deviceId + "/credentials";
        String s2 = OkHttpUtil.get(url2, null, token);
        JSONObject jsonObject5 = JSONObject.parseObject(s2);
        Map<String, String> map1 = JSON.parseObject(jsonObject5.toJSONString(), Map.class);
        map1.put("credentialsType", tokenType);
        map1.put("credentialsValue", token1);
        OkHttpUtil.postJsonParams(url2, s2, token);
        int i = roleService.updateToken1(token1, deviceId);
        return toAjax(i);
    }

    /**
     * 删除设备
     *
     * @param ids
     * @return
     */
    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        User user = getSysUser();
        String token = user.getAuthorization();
        String url1 = url + "/api/device/";
        String[] idss = ids.split(",");
        int i1 = 0;
        if (idss.length == 1) {
            OkHttpUtil.delete(url1 + idss[0], null, token);
            i1 = roleService.deleteDevice(idss[0]);
        } else {
            for (int i = 0; i < idss.length; i++) {

                OkHttpUtil.delete(url1 + idss[i], null, token);
                i1 = roleService.deleteDevice(idss[i]);
                System.out.println(url1 + idss[i]);
            }
        }

        return toAjax(i1);
    }


    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @PostMapping("/removeSelectAll")
    @ResponseBody
    public AjaxResult removeAll(String ids) {
        User user = getSysUser();
        String token = user.getAuthorization();
        String url1 = url + "/api/device/";
        String[] idss = ids.split(",");
        int i1 = 0;
        if (idss.length == 1) {
            String deviceId = roleService.pileassetbyChipId(idss[0]).getDeviceId();
            OkHttpUtil.delete(url1 + deviceId, null, token);
            i1 = roleService.deleteDevice(deviceId);
        } else {
            for (int i = 0; i < idss.length; i++) {
                String deviceId = roleService.pileassetbyChipId(idss[i]).getDeviceId();
                OkHttpUtil.delete(url1 + deviceId, null, token);
                i1 = roleService.deleteDevice(deviceId);
                System.out.println(url1 + deviceId);
            }
        }

        return toAjax(i1);
    }

    /**
     * 校验角色名称
     */
    @PostMapping("/checkRoleNameUnique")
    @ResponseBody
    public String checkRoleNameUnique(Role role) {
        return roleService.checkRoleNameUnique(role);
    }

    /**
     * 校验角色权限
     */
    @PostMapping("/checkRoleKeyUnique")
    @ResponseBody
    public String checkRoleKeyUnique(Role role) {
        return roleService.checkRoleKeyUnique(role);
    }

    /**
     * 选择菜单树
     */
    @GetMapping("/selectMenuTree")
    public String selectMenuTree() {
        return prefix + "/tree";
    }

    /**
     * 设备状态修改
     */
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(Device device) {
        User user = getSysUser();
        String token = user.getAuthorization();
        String url1 = url + "/api/plugins/telemetry/" + device.getDeviceId() + "/SERVER_SCOPE";
        OkHttpUtil.postJsonParams(url1, device.getStatus(), token);
        return toAjax(1);
    }

    /**
     * 分配用户
     */
    @GetMapping("/authUser/{deviceId}")
    public String authUser(@PathVariable("deviceId") String deviceId, ModelMap mmap) {
        User user = getSysUser();
        String token = user.getAuthorization();
        String url1 = url + "/api/customers";
        OkHttpUtil.get(url1, null, token);
        return prefix + "/authUser";
    }

    /**
     * 解锁单个设备钱包
     *
     * @param chipId
     * @return
     */
    @PostMapping("/unlock/{chipId}")
    @ResponseBody
    public AjaxResult unlock(@PathVariable("chipId") String chipId) {
        Pileasset pileasset = roleService.pileassetbyChipId(chipId);
        String unlockStr = "{\"method\": \"personal_unlockAccount\", \"params\": [\"" + pileasset.getFromWallet() + "\", \"" + walletPassword + "\", 0],\"id\":1,\"jsonrpc\":\"2.0\"}";
        String s = OkHttpUtil.postJsonParams1(pileasset.getPeerUrl(), unlockStr);
        if (s.contains("error") || !s.contains("sucess")) {
            String error = JSONObject.parseObject(s).getJSONObject("error").getString("message");
            return error(error);
        } else {
            return success("解锁成功");
        }
    }

    //钱包解锁
    @PostMapping("/unlockALL")
    public AjaxResult unlockAll(String chipIds) {
        try {
            String[] ids = chipIds.split(",");
            for (int i = 0; i < ids.length; i++) {
                Pileasset pileasset = roleService.pileassetbyChipId(ids[i]);
                String unlockStr = "{\"method\": \"personal_unlockAccount\", \"params\": [\"" + pileasset.getFromWallet() + "\", \"" + walletPassword + "\", 0],\"id\":1,\"jsonrpc\":\"2.0\"}";
                String s = OkHttpUtil.postJsonParams1(pileasset.getPeerUrl(), unlockStr);
            }
        } catch (Exception e) {
            return error();
        }


        return success();

    }

    /**
     * 查询已分配用户角色列表
     */
    @PostMapping("/authUser/allocatedList")
    @ResponseBody
    public TableDataInfo allocatedList(User user) {
        startPage();
        List<User> list = userService.selectAllocatedList(user);
        return getDataTable(list);
    }

    /**
     * 取消授权
     */
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PostMapping("/authUser/cancel")
    @ResponseBody
    public AjaxResult cancelAuthUser(UserRole userRole) {
        return toAjax(roleService.deleteAuthUser(userRole));
    }

    /**
     * 批量取消授权
     */
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PostMapping("/authUser/cancelAll")
    @ResponseBody
    public AjaxResult cancelAuthUserAll(Long roleId, String userIds) {
        return toAjax(roleService.deleteAuthUsers(roleId, userIds));
    }

    /**
     * 选择用户
     */
    @GetMapping("/authUser/selectUser/{roleId}")
    public String selectUser(@PathVariable("roleId") Long roleId, ModelMap mmap) {
        mmap.put("role", roleService.selectRoleById(roleId));
        return prefix + "/selectUser";
    }

    /**
     * 查询未分配用户角色列表
     */
    @PostMapping("/authUser/unallocatedList")
    @ResponseBody
    public TableDataInfo unallocatedList(User user) {
        startPage();
        List<User> list = userService.selectUnallocatedList(user);
        return getDataTable(list);
    }

    /**
     * 批量选择用户授权
     */
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PostMapping("/authUser/selectAll")
    @ResponseBody
    public AjaxResult selectAuthUserAll(Long roleId, String userIds) {
        return toAjax(roleService.insertAuthUsers(roleId, userIds));
    }

    /**
     * 遥测数据上链
     *
     * @param json
     * @return
     */
    @PostMapping("Telemetry ")
    @ResponseBody
    public JSONObject Telemetry(@RequestBody String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        String chipID = jsonObject.getString("ChipID");
        jsonObject.remove("ChipID");
        String status = jsonObject.toJSONString();
        String hex = "0x" + Hex.str2HexStr(status);
        Pileasset pileasset = roleService.pileassetbyChipId(chipID);
        String sendTransactionStr = "{\"jsonrpc\":\"2.0\",\"method\":\"eth_sendTransaction\",\"params\":[{\n" +
                "  \"from\": \"" + pileasset.getToWallet() + "\",\n" +
                "  \"to\": \"" + pileasset.getFromWallet() + "\",\n" +
                "  \"value\":\"" + balance + "\",\n" +
                "  \"data\":\"" + hex + "\"\n" +
                "},\"bld123\"],\"id\":11}";
        String s = OkHttpUtil.postJsonParams1(pileasset.getPeerUrl(), sendTransactionStr);
        JSONObject jsonObject1 = JSONObject.parseObject(s);
        jsonObject1.replace("result", "TXhash");
        return jsonObject1;
    }


    @Autowired
    private ConnConfig connConfig;

    @PostMapping("/downFile")
    @ResponseBody
    public void downAndReadFile(@RequestBody String address, HttpServletResponse response) throws IOException {
        String filePath = "";
        String fileName = "";
        InputStream inputStream = null;
        OutputStream out = null;
        Connection conn = null;
        String address1 = JSONObject.parseObject(address).getString("address");
        try {

            conn = new Connection(ip, port);
            boolean login = connConfig.login();
            System.out.println(login);
            conn.connect();
            boolean b = conn.authenticateWithPassword(user, password);
            List<String> fileProperties = connConfig.getFileProperties(conn, "/usr/local/block-chain/data0/keystore/");
            for (int i = 0; i < fileProperties.size(); i++) {
                if (fileProperties.get(i).startsWith("UTC")) {
                    String substring = "0x" + fileProperties.get(i).substring((fileProperties.get(i).length() - 40), fileProperties.get(i).length());
                    System.out.println(substring);
                    if (substring.equals(address1)) {
                        fileName = fileProperties.get(i);
                        filePath = "/usr/local/block-chain/data0/keystore/" + fileName;
                        break;
                    }
                } else {
                    continue;
                }

            }
            System.out.println(filePath);
            System.out.println(fileName);
            //String filePath1="D:\\aab\\UTC--2020-05-27T09-31-26.464938536Z--f86f0438dbf2ae697836944617d66680dab68d36";
            InputStream is = new FileInputStream(filePath);
            byte[] buffer = new byte[1024];
            //设置文件ContentType类型，这样设置，会自动判断下载文件类型
            response.setContentType("multipart/form-data");
            //设置文件头：最后一个参数是设置下载文件名（设置编码格式防止下载的文件名乱码）
            response.setHeader("Content-Disposition", "attachment;fileName=" + new String(fileName.getBytes("UTF-8"), "ISO8859-1"));
            ServletOutputStream outputStream = response.getOutputStream();
            int c = 0;
            while (c != -1) {
                c = is.read(buffer);
                //写到输出流(out)中
                outputStream.write(buffer, 0, c);
            }
            connConfig.copyFile(conn, filePath, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
//        JSONObject j = new JSONObject();
//        j.put("method", "personal_sendTransaction");
//        JSONObject params = new JSONObject();
//        params.put("from", "0x391694e7e0b0cce554cb130d723a9d27458f9298");
//        params.put("to", "0xafa3f8684e54059998bc3a7b0d2b0da075154d66");
//        params.put("value", 123456789);
//
//        j.put("params", params);
//        System.out.println(j.toJSONString());
//        System.out.println(params.toJSONString());
        String str = "这是测试1";
        String hex = "0x" + Hex.str2HexStr(str);
        System.out.println(hex);
//        1    1000111111011001 110011000101111 110110101001011 1000101111010101 110001
//        2    1000111111011001 110011000101111 110110101001011 1000101111010101 110010
//        {"method":"personal_sendTransaction","params":{"from":"0x391694e7e0b0cce554cb130d723a9d27458f9298","to":"0xafa3f8684e54059998bc3a7b0d2b0da075154d66","value":123456789}}
    }

    /**
     * @param blockDevice 参数1
     * @param resJson     参数2
     * @return void
     * @author SOFAS
     * @date 2020/7/3
     * @directions 更新区块链记录
     */
    @Async
    public void asyncUpdateBlock(BlockDevice blockDevice, JSONObject resJson) {
        Object newHash = resJson.get("result");
        BlockHash bh = new BlockHash();
        bh.setChipId(blockDevice.getChipId());
        bh.setFromWallet(blockDevice.getToWallet());
        bh.setToWallet(blockDevice.getDeviceWallet());
        bh.setHash(newHash.toString());

        BlockDevice whereBd = new BlockDevice();
        whereBd.setId(blockDevice.getId());
        BlockDevice updateBd = new BlockDevice();
        String time = String.valueOf(System.currentTimeMillis());
        /*更新余额*/
        Long dm = blockDevice.getDeviceMoney();
        String substring = balance.substring(2);
        BigInteger bigint = new BigInteger(substring, 16);
        long money = bigint.longValue();
        dm += money;
        updateBd.setDeviceMoney(dm);
        bh.setMoney(money);

        if (blockDeviceMapper.update(updateBd, whereBd) < 1) {
            log.error("更新区块链设备余额失败，id：{}， 金额:：{}", blockDevice.getId(), money);
        }
        if (blockHashMapper.add(bh) < 1) {
            log.error("添加设备区块链历史交易记录失败，参数：{}", JSONObject.toJSONString(bh));
        }
    }


    //    -------------------------------许萌写的设备操作相关接口--------------------------------------------
    @PostMapping("/upgradeVersion")
    @ResponseBody
    public String upgradeVersion(@RequestParam String Version, @RequestParam String ChipID) {
        String s = roleService.upgradeVersion(ChipID, Version);
        return s;
    }

    /**
     * 数据上链
     * bld数据上链，之前的老哥写在这里，时间不够就不做多的修改
     */
    @PostMapping("block")
    @ResponseBody
    public JSONObject block(@RequestBody JSONObject param) throws IOException, CipherException {
        String chipId = param.getString("ChipID");
        BlockDevice bd = new BlockDevice();
        bd.setChipId(chipId);
        List<BlockDevice> list = blockDeviceMapper.select(bd);
        if (list == null || list.size() < 1) {
            return JSONObject.parseObject("该chipId没有对应的设备");
        }
        BlockDevice blockDevice = list.get(0);

        ResultInfo<String> br = BlockUtils.blockTransaction("a963d384cac4927a4f632be6d51c74e3d549538f", blockDevice.getDeviceWallet(), new BigInteger("10000", 10), BlockUtils.gas, BlockUtils.gas_limit, param.toJSONString());
        String data = br.getData();

        JSONObject res = new JSONObject();
        if (br.isSuccess() && !StringUtils.isNullString(data)){
            res.put("result", data);
            asyncUpdateBlock(blockDevice, res);
        }else {
            res.put("message", br.getMessage());
        }
        return res;
    }

    @Resource
    private DeviceService deviceService;

    //保存
    @PostMapping("/save")
    @ResponseBody
    public Results save(@RequestBody String json) throws NoSuchAlgorithmException, CipherException, InvalidAlgorithmParameterException, NoSuchProviderException, IOException {
        Results<Object> result = new Results<>();
        result.setSmartMeter1("registration failed");
        JSONObject j = JSONObject.parseObject(json);
        String chipId = j.getString("ChipID");
        String apiKey = j.getString("APIKEY");
        String wv = j.getString("WorkingVersion");
        String sn1 = j.getString("SN1");
        String sn2 = j.getString("SN2");
        String sn = j.getString("SN");
        if (StringUtils.isNullString(sn1) && !StringUtils.isNullString(sn)){
            sn1 = sn;
        }
        /*参数检查*/
        if (StringUtils.isNullString(chipId) || StringUtils.isNullString(apiKey)){
            return result;
        }
        if (!"tPmAT5Ab3j7F9".equals(apiKey)) {
            return result;
        }
        List<BlockDevice> select = blockDeviceMapper.select(new BlockDevice(chipId));
        Pileasset pileasset = roleService.pileassetbyChipId(chipId);
        if (select != null && select.size() > 0) {
            BlockDevice bd = select.get(0);
            BlockDevice update = new BlockDevice();
            update.setDeviceVersion(wv);
            boolean f1 = !StringUtils.isNullString(wv);
            if (f1){
                update.setDeviceVersion(wv);
            }
            boolean f2 = !StringUtils.isNullString(sn1);
            if (f2){
                update.setSn1(sn1);
            }else {
                sn1 = bd.getSn1();
            }
            boolean f3 = !StringUtils.isNullString(sn2);
            if (f3){
                update.setSn2(sn2);
            }else {
                sn2 = bd.getSn2();
            }
            if (StringUtils.isNullString(bd.getDeviceWallet())){
                Block wallet = BlockUtils.getWallet();
                String addr = wallet.getAddr();
                if (!StringUtils.isNullString(addr)){
                    update.setDeviceWallet(addr);
                }
            }
            if(f1 || f2 || f3){
                return blockDeviceMapper.update(update, new BlockDevice(chipId)) > 0 ? new Results<>(sn1, sn2, pileasset.getToken(), bd.getToWallet(), bd.getDeviceWallet()) : result;
            }else {
                return new Results<>(sn1, sn2, pileasset.getToken(), bd.getToWallet(), bd.getDeviceWallet());
            }
        } else {
            Pileasset pt = new Pileasset();
            pt.setChipId(chipId);
            String spicapacity = j.getString("SPICAPACITY");
            if (!StringUtils.isNullString(spicapacity)) {
                pt.setSpicapacity(Long.parseLong(spicapacity));
            }
            if (!StringUtils.isNullString(wv)) {
                pt.setWorkingVersion(wv);
            }
            String uv = j.getString("UpdateVersion");
            if (!StringUtils.isNullString(uv)) {
                pt.setUpdateVersion(uv);
            }
            String ccId = j.getString("CCID");
            if (!StringUtils.isNullString(ccId)) {
                pt.setCcid(ccId);
            }
            String imei = j.getString("IMEI");
            if (!StringUtils.isNullString(imei)) {
                pt.setImei(imei);
            }
            sn1 = StringUtils.isNullString(sn1) ? "000000000000" : sn1;
            pt.setSn1(sn1);
            sn2 = StringUtils.isNullString(sn2) ? "000000000000" : sn2;
            pt.setSn2(sn2);
            String sd = j.getString("SD");
            if (!StringUtils.isNullString(sd)) {
                pt.setSd(sd);
            }
            String gps = j.getString("GPS");
            if (!StringUtils.isNullString(gps)) {
                pt.setGps(gps);
            }
            String pd = j.getString("PD");
            if (!StringUtils.isNullString(pd)) {
                pt.setPd(pd);
            }
            String op = j.getString("OP");
            if (!StringUtils.isNullString(op)) {
                pt.setOp(op);
            }
            String sp = j.getString("SP");
            if (!StringUtils.isNullString(sp)) {
                pt.setSp(sp);
            }
            String ct = j.getString("Customer");
            if (!StringUtils.isNullString(ct)) {
                pt.setCustomer(ct);
            }
            String mm = j.getString("MultiMeter");
            if (!StringUtils.isNullString(mm)) {
                pt.setMultiMeter(mm);
            }

            /*登陆自动注册帐号获取token*/
            String url1 = url + "/api/auth/login";
            String jsonParams = "{\"username\":\"" + auotoAddUserName + "\"," + "\"password\":\"" + auotoAddPassword + "\"}";
            String s = OkHttpUtil.postJsonParams(url1, jsonParams, "");
            JSONObject loginJ = JSONObject.parseObject(s);
            Object tokenO = loginJ.get("token");
            String token;
            if (!StringUtils.isNullString(tokenO)){
                token = "Bearer " + tokenO.toString();
            }else {
                return result;
            }
            /*注册设备*/
            BlockDevice bd = new BlockDevice(chipId);
            List<BlockDevice> bds = blockDeviceMapper.select(bd);
            if(bds == null || bds.size() < 1){
                String type = String.valueOf(pt.getSpicapacity()).startsWith("8") ? "V2" : "V1";
                ResultInfo resultInfo = deviceService.autoAddDevice(chipId, type, token, pt.getWorkingVersion(), sn1, sn2, false);
                if (!resultInfo.isSuccess()){
                    return result;
                }
                bd = JSONObject.parseObject(JSONObject.toJSONString(resultInfo.getData()), BlockDevice.class);
            }else {
                bd = bds.get(0);
            }

            pt.setChipId(chipId);
            pt.setFromWallet(bd.getDeviceWallet());
            pt.setToWallet(bd.getToWallet());
            pt.setToken(bd.getDeviceToken());
            /*添加失败则返回*/
            if (pileasset == null){
                if (roleService.save(pt) < 1){
                    return result;
                }
            }else {
                if (roleService.updatePileasset(pt) < 1){
                    return result;
                }
            }
            return new Results<>(sn1, sn2, bd.getDeviceToken(), bd.getToWallet(), bd.getDeviceWallet());
        }
    }
}