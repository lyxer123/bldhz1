//package com.bld.project.system.xitaChain.utils;
//
//import com.alibaba.fastjson.JSONObject;
//import com.bld.common.utils.DateUtils;
//import com.bld.common.utils.StringUtils;
//import com.bld.framework.utils.OkHttpUtil;
//import com.bld.framework.web.domain.ResultInfo;
//import com.bld.project.monitor.server.domain.Sys;
//import lombok.extern.slf4j.Slf4j;
//
//import java.math.BigInteger;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//
///**
// * @author SOFAS
// * @date 2020/5/11
// * @directions 溪塔区块链工具类
//*/
//@Slf4j
//public class XiTaChainUtils {
////    ---------------------------------------------------帐号注册--------------------------------------------
//
//    /**
//     * @author SOFAS
//     * @date   2020/5/11
//     * @directions  根据账户id和盐生成签名
//     * @param bizAccountId  账户id
//     * @param salt  自定义盐
//     * @return  com.bld.framework.web.domain.ResultInfo
//     */
//    public static String getSignature(String bizAccountId, String salt) {
//        String str = bizAccountId + salt;
//        try {
//            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
//            md.update(str.getBytes());
//            return new BigInteger(1, md.digest()).toString(16);
//        } catch (Exception e) {
//            throw new RuntimeException("MD5 error");
//        }
//    }
//
//    /**
//     * @author SOFAS
//     * @date   2020/5/11
//     * @directions  根据账户id和签名生成token
//     * @param bizAccountId  账户id
//     * @param signature  签名
//     * @return  com.bld.framework.web.domain.ResultInfo
//     */
//    public static ResultInfo createKeyAndSecret(String bizAccountId, String signature) {
//        JSONObject j = new JSONObject();
//        j.put("bizAccountId", bizAccountId);
//        j.put("signature", signature);
//        ResultInfo resultInfo = OkHttpUtil.bldPostJsonParams("http://192.168.3.13:7001/api/public/authorized_access", j.toJSONString());
//        return resultInfo;
//    }
//
//    /**
//     * @author SOFAS
//     * @date   2020/5/11
//     * @directions  根据key和secret创建token
//     * @param key  accessKey
//     * @param secret  accessSecret
//     * @return  com.bld.framework.web.domain.ResultInfo
//     */
//    public static ResultInfo createToken(String key, String secret){
//        JSONObject j = new JSONObject();
//        j.put("accessKey", key);
//        j.put("accessSecret", secret);
//        ResultInfo resultInfo = OkHttpUtil.bldPostJsonParams("http://192.168.3.13:7001/api/v1/access", j.toJSONString());
//        return resultInfo;
//    }
//
//    /**
//     * @author SOFAS
//     * @date   2020/5/11
//     * @directions   创建账户
//     * @param token  通行证
//     * @return  com.bld.framework.web.domain.ResultInfo
//     */
//    public static ResultInfo createAccount(String token){
//        HashMap<String, String> headerMap = new HashMap<>();
//        headerMap.put("x-Authorization", token);
//        headerMap.put("accept", "*/*");
//        return OkHttpUtil.bldPostJsonParams("http://192.168.3.13:7001/api/v1/kms_key", "", headerMap);
//    }
//
////    -------------------------------------------------上链-------------------------------------------
//
//    /**
//     * @author SOFAS
//     * @date   2020/5/11
//     * @directions   原文上链存储
//     * @param token  通行证
//     * @return  com.bld.framework.web.domain.ResultInfo
//     */
//    public static ResultInfo evidences(String token){
//        JSONObject paramJ = new JSONObject();
//        paramJ.put("bizId", "2");
//        paramJ.put("origin", "测试上链信息2");
//        HashMap<String, String> headerMap = new HashMap<>();
//        headerMap.put("x-Authorization", token);
//        headerMap.put("accept", "*/*");
//        headerMap.put("Content-Type", "application/json");
//        return OkHttpUtil.bldPostJsonParams("http://192.168.3.13:7001/api/v1/origin/evidences", paramJ.toJSONString(), headerMap);
//    }
//
//    /**
//     * @author SOFAS
//     * @date   2020/5/11
//     * @directions  将文件转换成base64进行存证
//     * @param token  通行证
//     * @param param  参数
//     * @return  com.bld.framework.web.domain.ResultInfo
//     */
//    public static ResultInfo evidencesBase64(String token, String param){
//        HashMap<String, String> headerMap = new HashMap<>();
//        headerMap.put("x-Authorization", token);
//        headerMap.put("accept", "*/*");
//        headerMap.put("Content-Type", "application/json");
//        ResultInfo resultInfo = OkHttpUtil.bldPostJsonParams("http://192.168.3.13:7001/api/v1/evidences", param, headerMap);
//        return resultInfo;
//    }
//
//    /**
//     * @author SOFAS
//     * @date   2020/5/12
//     * @directions  将文件转换成base64进行存证（异步）
//     * @param token  通行证
//     * @param param  参数
//     * @return  com.bld.framework.web.domain.ResultInfo
//     */
//    public static ResultInfo evidencesBase64Async(String token, String param){
//        HashMap<String, String> headerMap = new HashMap<>();
//        headerMap.put("x-Authorization", token);
//        headerMap.put("accept", "*/*");
//        headerMap.put("Content-Type", "application/json");
//        ResultInfo resultInfo = OkHttpUtil.bldPostJsonParams("http://192.168.3.13:7001/api/v1/evidences/async", param, headerMap);
//        return resultInfo;
//    }
//
////    -----------------------------------------------上链查询--------------------------------------------------
//
//    /**
//     * @author SOFAS
//     * @date   2020/5/12
//     * @directions  原文上链结果查询
//     * @param paramMap  参数
//     * @param headerMap  请求头
//     * @return  com.bld.framework.web.domain.ResultInfo
//     */
//    public static ResultInfo query(HashMap<String, String> paramMap, HashMap<String, String> headerMap){
//        String s = OkHttpUtil.get("http://192.168.3.13:7001/api/v1/origin/evidences", paramMap, headerMap);
//        if (StringUtils.isNotBlank(s)){
//            JSONObject j = JSONObject.parseObject(s);
//            int code = Integer.parseInt(String.valueOf(j.get("code")));
//            ResultInfo<Object> resultInfo = new ResultInfo<>();
//            resultInfo.setCode(code);
//            resultInfo.setSuccess(code == 200);
//            resultInfo.setData(j.get("data"));
//            resultInfo.setMessage(j.get("exception").toString());
//            return resultInfo;
//        }
//        return ResultInfo.error("查询失败");
//    }
//
//    public static ResultInfo queryBase64(HashMap<String, String> paramMap, HashMap<String, String> headerMap){
//        String s = OkHttpUtil.get("http://192.168.3.13:7001/api/v1/evidences", paramMap, headerMap);
//        if (StringUtils.isNotBlank(s)){
//            JSONObject j = JSONObject.parseObject(s);
//            int code = Integer.parseInt(String.valueOf(j.get("code")));
//            ResultInfo<Object> resultInfo = new ResultInfo<>();
//            resultInfo.setCode(code);
//            resultInfo.setSuccess(code == 200);
//            resultInfo.setData(j.get("data"));
//            resultInfo.setMessage(j.get("exception").toString());
//            return resultInfo;
//        }
//        return ResultInfo.error("查询失败");
//    }
//
////    --------------------------------------------------------------存证授权------------------------------------
//
//    /**
//     * @author SOFAS
//     * @date   2020/5/12
//     * @directions  将存证授权给其他用户指定时间
//     * @param headerMap  请求头
//     * @param paramStr  请求参数
//     * @return  com.bld.framework.web.domain.ResultInfo
//     */
//    public static ResultInfo authorize(HashMap<String, String> headerMap, String paramStr){
//        return OkHttpUtil.bldPostJsonParams("http://192.168.3.13:7001/api/v1/evidences/authorization", paramStr, headerMap);
//    }
//
////    -------------------------------------------------------查询存证授权信息------------------------------------------
//
//    /**
//     * @author SOFAS
//     * @date   2020/5/12
//     * @directions  查询当指定账户的存证授权列表
//     * @param token  指定账户的token
//     * @return  com.bld.framework.web.domain.ResultInfo
//     */
//    public static ResultInfo authorizedList(String token){
//        HashMap<String, String> headerMap = new HashMap<>();
//        headerMap.put("x-Authorization", token);
//        String s = OkHttpUtil.get("http://192.168.3.13:7001/api/v1/evidences/authorized_list", null, headerMap);
//        if (StringUtils.isNotBlank(s)){
//            JSONObject j = JSONObject.parseObject(s);
//            int code = Integer.parseInt(String.valueOf(j.get("code")));
//            ResultInfo<Object> resultInfo = new ResultInfo<>();
//            resultInfo.setCode(code);
//            resultInfo.setSuccess(code == 200);
//            resultInfo.setData(j.get("data"));
//            resultInfo.setMessage(j.get("exception").toString());
//            return resultInfo;
//        }
//        return ResultInfo.error("查询失败");
//    }
//
//    /**
//     * @author SOFAS
//     * @date   2020/5/12
//     * @directions  查询当指定账户的存证授权用户列表
//     * @param token  指定账户的token
//     * @return  com.bld.framework.web.domain.ResultInfo
//     */
//    public static ResultInfo authorizedUserList(String token){
//        HashMap<String, String> headerMap = new HashMap<>();
//        headerMap.put("x-Authorization", token);
//        String s = OkHttpUtil.get("http://192.168.3.13:7001/api/v1/evidences/authorized_user_list", null, headerMap);
//        if (StringUtils.isNotBlank(s)){
//            JSONObject j = JSONObject.parseObject(s);
//            int code = Integer.parseInt(String.valueOf(j.get("code")));
//            ResultInfo<Object> resultInfo = new ResultInfo<>();
//            resultInfo.setCode(code);
//            resultInfo.setSuccess(code == 200);
//            resultInfo.setData(j.get("data"));
//            resultInfo.setMessage(j.get("exception").toString());
//            return resultInfo;
//        }
//        return ResultInfo.error("查询失败");
//    }
//
//    public static ResultInfo authorization(HashMap<String, String> headerMap, String paramStr){
//        String s = OkHttpUtil.get("http://192.168.3.13:7001/api/v1/evidences/authorization?authorizeAppKey=oYjNLXVmxaynPyeB", null, headerMap);
//        if (StringUtils.isNotBlank(s)){
//            JSONObject j = JSONObject.parseObject(s);
//            int code = Integer.parseInt(String.valueOf(j.get("code")));
//            ResultInfo<Object> resultInfo = new ResultInfo<>();
//            resultInfo.setCode(code);
//            resultInfo.setSuccess(code == 200);
//            resultInfo.setData(j.get("data"));
//            resultInfo.setMessage(j.get("exception").toString());
//            return resultInfo;
//        }
//        return ResultInfo.error("查询失败");
//    }
//}
