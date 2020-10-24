package com.bld.project.utils;

import com.alibaba.fastjson.JSONObject;
import com.bld.common.utils.StringUtils;
import com.bld.common.utils.security.ShiroUtils;
import com.bld.common.utils.spring.SpringUtils;
import com.bld.framework.utils.OkHttpUtil;
import com.bld.framework.web.domain.ResultInfo;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author SOFAS
 * @date 2020/5/7
 * @directions  获取thingsboard API对应链接
*/
@Slf4j
@Component
public class TbApiUtils {
    /**
     * tb服务器
     */
    private static final String THINGS_BOARD_HOST = "http://125.64.98.21:8088";
    /**
     * 本地
     */
//    private static final String THINGS_BOARD_HOST = "http://localhost:8088";

    public static JSONObject get(String url){
        ResultInfo resultInfo = bldGet(url);
        String s;
        if (resultInfo.isSuccess()){
            s = resultInfo.getData().toString();
        }else {
            s = resultInfo.getMessage();
        }
        JSONObject j = null;
        try {
            j = JSONObject.parseObject(s);
        } catch (Exception e) {
            if (e.getMessage().contains("syntax error")){
                j = new JSONObject();
                j.put("data", s);
            }else {
                log.error("***************************请求返回转换为json格式失败***********************");
                e.printStackTrace();
            }
        }
        return j;
    }

    public static ResultInfo<String> bldGet(String url){
        return bldGet(url, ShiroUtils.getTbToken());
    }

    public static ResultInfo<String> bldGet(String url, String token){
        Request.Builder builder = new Request.Builder().url(url);
        builder.addHeader("X-Authorization", token);
        Request request = builder.build();

        Response response = null;
        try {
            OkHttpClient okHttpClient = SpringUtils.getBean(OkHttpClient.class);
            response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String string = response.body().string();
                return ResultInfo.success(string, "");
            }else {
                String string = response.body().string();
                if (string.contains("User is already active")){
                    string = "用户已激活";
                }else if (string.contains("Device with requested id wasn't found")){
                    string = "没有该设备";
                }
                return ResultInfo.error(string);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultInfo.error("请求错误");
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }


    public static HashMap<String, String> getHeader(String token){
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("X-Authorization", token);
        return headerMap;
    }

    public static List getList(String url, Map<String, String> paramMap){
        String s = OkHttpUtil.get(url, paramMap, ShiroUtils.getSysUser().getAuthorization());
        return JSONObject.parseObject(s, List.class);
    }

    public String loginTb(){
        String url1 = THINGS_BOARD_HOST + "/api/auth/login";
        String jsonParams = "{\"username\":\"1005750@qq.com\",\"password\":\"123654\"}";
        String s = OkHttpUtil.postJsonParams(url1, jsonParams, "");
        JSONObject loginJ = JSONObject.parseObject(s);
        String token = loginJ.getString("token");
        return StringUtils.isNullString(token) ? null : "Bearer " + token;
    }


//    ---------------------------------------------------用户--------------------------------------------
    /**
     * @author SOFAS
     * @date   2020/5/7
     * @directions  保存用户信息api
     * @return  java.lang.String
     */
    public static String updateUserInfoApi(){
        return THINGS_BOARD_HOST + "/api/user";
    }

    /**
     * @author SOFAS
     * @date   2020/5/9
     * @directions  添加用户api
     * @param sendEmail  是够发送激活邮件，不发送激活邮件则会返回激活链接，都可以重复获取
     * @return  java.lang.String
     */
    public static String addUserInfoApi(boolean sendEmail){
        return THINGS_BOARD_HOST + "/api/user?sendActivationMail=" + sendEmail;
    }

    /**
     * @author SOFAS
     * @date   2020/5/9
     * @directions  根据用户id删除用户api
     * @param tbUserId  用户id
     * @return  java.lang.String
     */
    public static String deleteUserInfoApi(String tbUserId){
        return THINGS_BOARD_HOST + "/api/user/" + tbUserId;
    }

    /**
     * @author SOFAS
     * @date   2020/5/7
     * @directions  获取当前登录用户信息API
     * @return  java.lang.String
     */
    public static String getCurrentUserInfoApi(){
        return THINGS_BOARD_HOST + "/api/auth/user";
    }

    /**
     * @author SOFAS
     * @date   2020/5/9
     * @directions  修改当前登录用户密码api
     * @return  java.lang.String
     */
    public static String modifyPassword(){
        return THINGS_BOARD_HOST + "/api/auth/changePassword";
    }

    /**
     * @author SOFAS
     * @date   2020/5/9
     * @directions 获取用户激活链接api
     * @return  java.lang.String
     */
    public static String getUserActivationUrl(String tbUserId){
        return THINGS_BOARD_HOST + "/api/user/" + tbUserId + "/activationLink";
    }

    /**
     * @author SOFAS
     * @date   2020/5/26
     * @directions  获取根据客户ID查询旗下用户列表api
     * @param limit
     * @param search
     * @param customerId
     * @return  java.lang.String
     */
    public static String getUserListByCustomerIdApi(Integer limit, String search, String customerId){
        return THINGS_BOARD_HOST + "/api/customer/" + customerId + "/users?limit=" + limit + "&textSearch=" + (StringUtils.isBlank(search) ? "" : search);
    }

    public static String modifyUserStatusApi(String userId, boolean status){
        return THINGS_BOARD_HOST + "/api/user/" + userId + "/userCredentialsEnabled?userCredentialsEnabled=" + status;
    }

    public static String getUrlApi(String userId){
        return THINGS_BOARD_HOST + "/api/user/" + userId + "/activationLink";
    }

    public static String loginUserApi(String userId){
        return THINGS_BOARD_HOST + "api/user/" + userId + "/token";
    }


//    ---------------------------------------------------客户---------------------------------------------
    /**
     * @author SOFAS
     * @date   2020/5/7
     * @directions  租户查询旗下客户的列表
     * @param limit  每一页查询数量
     * @param textSearch  查询条件
     * @return  java.lang.String
     */
    public static String selectCustomerApi(int limit, String textSearch){
        return THINGS_BOARD_HOST + "/api/customers?limit=" + limit + "&textSearch=" + (StringUtils.isBlank(textSearch) ? "" : textSearch);
    }

    /**
     * @author SOFAS
     * @date   2020/5/7
     * @directions  租户删除旗下客户
     * @param userId  需要删除的客户id
     * @return  java.lang.String
     */
    public static String deleteCustomerApi(String userId){
        return THINGS_BOARD_HOST + "/api/customer/" + userId;
    }

    /**
     * @author SOFAS
     * @date   2020/5/8
     * @directions  租户添加新的结客户
     * @return  java.lang.String
     */
    public static String saveCustomerApi(){
        return THINGS_BOARD_HOST + "/api/customer";
    }

    /**
     * @author SOFAS
     * @date   2020/6/20
     * @directions  根据客户id获取客户名称
     * @param id
     * @return  java.lang.String
     */
    public static String getCustomerNameApi(String id){
        return THINGS_BOARD_HOST + "/api/customer/" + id + "/shortInfo";
    }

    public static String getCustomerAttributes(String customerId){
        return THINGS_BOARD_HOST + "/api/plugins/telemetry/CUSTOMER/" + customerId + "/values/attributes/SERVER_SCOPE";
    }

    public static String updateCustomerAttributes(String customerId){
        return THINGS_BOARD_HOST + "/api/plugins/telemetry/CUSTOMER/" + customerId + "/SERVER_SCOPE";
    }

//    ---------------------------------------------------资产---------------------------------------------

    /**
     * @author SOFAS
     * @date   2020/5/27
     * @directions  查询资产列表api
     * @param limit  每页查询数量
     * @param search  插叙条件
     * @return  java.lang.String
     */
    public static String assetsListApi(int limit, String search){
        return THINGS_BOARD_HOST + "/api/tenant/assets?limit=" + limit + "&textSearch=" + search;
    }

    /**
     * @author SOFAS
     * @date   2020/5/27
     * @directions  查询资产类型列表api
     * @return  java.lang.String
     */
    public static String getAssetsTypesApi(){
        return THINGS_BOARD_HOST + "/api/asset/types";
    }

    /**
     * @author SOFAS
     * @date   2020/5/28
     * @directions  添加资产api
     * @return  java.lang.String
     */
    public static String addAssetApi(){
        return THINGS_BOARD_HOST + "/api/asset";
    }

    /**
     * @author SOFAS
     * @date   2020/5/28
     * @directions  删除资产api
     * @param assetId  资产id
     * @return  java.lang.String
     */
    public static String delAssetApi(String assetId){
        return THINGS_BOARD_HOST + "/api/asset/" + assetId;
    }

    /**
     * @author SOFAS
     * @date   2020/5/28
     * @directions  分配资产api
     * @param assetsId  资产id
     * @param customerId  客户id
     * @return  java.lang.String
     */
    public static String distributionAssets(String assetsId, String customerId){
        return THINGS_BOARD_HOST + "/api/customer/" + customerId + "/asset/" + assetsId;
    }

    public static String delDistribution(String assetsId){
        return THINGS_BOARD_HOST + "/api/customer/asset/" + assetsId;
    }

    public static String clientAssetsList(int limit, String search, String id){
        return THINGS_BOARD_HOST + "/api/customer/" + id + "/assets?limit=" + limit + "&textSearch=" + search;
    }

    public static  String publicAssetApi(String assetId){
        return THINGS_BOARD_HOST + "/api/customer/public/asset/" + assetId;
    }

    public static String privateAssetApi(String assetsId){
        return THINGS_BOARD_HOST + "/api/customer/asset/" + assetsId;
    }


    //    ---------------------------------------------------设备---------------------------------------------

    /**
     * @author SOFAS
     * @date   2020/5/7
     * @directions  通过设备token提交遥测数据api
     * @param dt  设备token
     * @return  java.lang.String
     */
    public static String saveOrUpdateTelemetryApi(String dt){
        return THINGS_BOARD_HOST + "/api/v1/" + dt + "/telemetry";
    }

    public static String searchDeviceApi(int limit, String search){
        return THINGS_BOARD_HOST + "/api/tenant/devices?limit=" + limit + (search == null ? "" : "&textSearch=" + search);
    }

    public static String delDeviceDistributionApi(String deviceId){
        return THINGS_BOARD_HOST + "/api/customer/device/" + deviceId;
    }

    public static String deviceDistributionApi(String customerId, String deviceId){
        return THINGS_BOARD_HOST + "/api/customer/" + customerId + "/device/" + deviceId;
    }

    public static String addDeviceApi(){
        return THINGS_BOARD_HOST + "/api/device";
    }

    public static String getDeviceTypesApi(){
        return THINGS_BOARD_HOST + "/api/device/types";
    }

    public static String delDevice(String deviceId){
        return THINGS_BOARD_HOST + "/api/device/" + deviceId;
    }

    public static String getDeviceListByCustomerIdApi(Integer limit, String search, String customerId){
        return THINGS_BOARD_HOST + "/api/customer/" + customerId + "/devices?limit=" + limit + "&textSearch=" + (StringUtils.isBlank(search) ? "" : search);
    }

    public static String getDeviceTokenApi(String deviceId){
        return THINGS_BOARD_HOST + "/api/device/" + deviceId + "/credentials";
    }

    public static String publicDeviceApi(String deviceId){
        return THINGS_BOARD_HOST + "/api/customer/public/device/" + deviceId;
    }

    public static String getDeviceAlarmApi(String deviceId, int limit, long startTime, long endTime, String searchStatus, boolean fetchOriginator, boolean ascOrder){
        return THINGS_BOARD_HOST + "/api/alarm/DEVICE/" + deviceId + "?limit=" + limit + "&startTime=" + startTime + "&endTime=" + endTime + "&searchStatus=" + searchStatus + "&fetchOriginator=" + fetchOriginator + "&ascOrder=" + ascOrder;
    }

    public static String getDeviceAlarmApi(String deviceId, String type, String tenantId, int limit, long startTime, long endTime){
        return THINGS_BOARD_HOST + "/api/events/DEVICE/" + deviceId + "/" + type + "?tenantId=" + tenantId + "&limit=" + limit + "&startTime=" + startTime + "&endTime=" + endTime;
    }

    /**
     * @author SOFAS
     * @date   2020/6/11
     * @directions  查询设备属性
     * @param deviceId  设备id
     * @param type  属性类型（SERVER_SCOPE 服务端属性，SHARED_SCOPE 共享属性）
     * @return  java.lang.String
     */
    public static String getDeviceAttributesApi(String deviceId, String type){
        return THINGS_BOARD_HOST + "/api/plugins/telemetry/DEVICE/" + deviceId + "/values/attributes/" + type;
    }

    /**
     * @author SOFAS
     * @date   2020/6/11
     * @directions  查询设备属性
     * @param deviceId  设备id
     * @param type  属性类型
     * @return  java.lang.String
     */
    public static String addDeviceAttributesApi(String deviceId, String type){
        return THINGS_BOARD_HOST + "/api/plugins/telemetry/DEVICE/" + deviceId + "/" + type;
    }

    public static String delDeviceAttributesApi(String deviceId, String k){
        return THINGS_BOARD_HOST + "/api/plugins/telemetry/DEVICE/" + deviceId + "/SERVER_SCOPE?keys=" + k;
    }

    public static String getTelemetrysApi(String keys, String deviceId, long startTime, long endTime){
        return THINGS_BOARD_HOST + "/api/plugins/telemetry/DEVICE/" + deviceId + "/values/timeseries?keys=" +keys + "&startTs=" + startTime + "&endTs=" + endTime;
    }


    //    ---------------------------------------------------租户---------------------------------------------
    /**
     * @author SOFAS
     * @date   2020/6/5
     * @directions  获取系统管理员账户下的租户管理员信息
     * @param limit  查询数量
     * @param search  查询条件
     * @return  java.lang.String
     */
    public static String getTenantListApi(int limit, String search){
        return THINGS_BOARD_HOST + "/api/tenants?limit=" + limit + "&textSearch=" + search;
    }

    public static String addTenantApi(){
        return THINGS_BOARD_HOST + "/api/tenant/";
    }

    public static String delTenantApi(String tenantId){
        return THINGS_BOARD_HOST + "/api/tenant/" + tenantId;
    }

    public static String updateTenantApi(){
        return THINGS_BOARD_HOST + "/api/tenant/";
    }

    /**
     * @author SOFAS
     * @date   2020/6/5
     * @directions  查询租户管理员列表
     * @param tenantId  租户id
     * @param limit  查询数量
     * @param search  查询条件
     * @return  java.lang.String
     */
    public static String getTenantAdminListApi(String tenantId, int limit, String search){
        return THINGS_BOARD_HOST + "/api/tenant/" + tenantId + "/users?limit=" + limit + "&textSearch=" + search;
    }
}
