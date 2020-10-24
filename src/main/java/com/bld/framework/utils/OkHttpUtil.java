package com.bld.framework.utils;

import com.alibaba.fastjson.JSONObject;
import com.bld.common.utils.spring.SpringUtils;
import com.bld.framework.web.domain.ResultInfo;
import okhttp3.*;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class OkHttpUtil {
    private static final Logger logger = LoggerFactory.getLogger(OkHttpUtil.class);



    /**
     * 根据map获取get请求参数
     * @param queries
     * @return
     */
    public static StringBuffer getQueryString(String url, Map<String,String> queries){
        StringBuffer sb = new StringBuffer(url);
        if (queries != null && queries.keySet().size() > 0) {
            boolean firstFlag = true;
            for (Map.Entry entry : queries.entrySet()) {
                if (firstFlag) {
                    sb.append("?").append(entry.getKey()).append("=").append(entry.getValue());
                    firstFlag = false;
                } else {
                    sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
                }
            }
        }
        return sb;
    }

    /**
     * 调用okhttp的newCall方法
     * @param request
     * @return
     */
    private static String execNewCall(Request request){
        Response response = null;
        try {
            OkHttpClient okHttpClient = SpringUtils.getBean(OkHttpClient.class);
            response = okHttpClient.newCall(request).execute();
            if (response.body() != null) {
                return response.body().string();
            }
        } catch (Exception e) {
            logger.error("okhttp3 put error >> ex = {}", ExceptionUtils.getStackTrace(e));
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return "";
    }

    /**
     * get
     * @param url     请求的url
     * @param queries 请求的参数，在浏览器？后面的数据，没有可以传null
     * @return
     */
    public static String get(String url, Map<String, String> queries, String token) {
        HashMap<String, String> headerMap = new HashMap<>(1);
        headerMap.put("X-Authorization", token);
        return get(url, queries, headerMap);
    }

    public static String get(String url, Map<String, String> queries, Map<String, String> headerMap) {
        StringBuffer sb = getQueryString(url,queries);
        Request.Builder builder = new Request.Builder().url(sb.toString());
        if (headerMap != null){
            for (Map.Entry<String, String> e : headerMap.entrySet()){
                builder.addHeader(e.getKey(), e.getValue());
            }
        }
        return execNewCall(builder.build());
    }

    /**
     * post
     *
     * @param url    请求的url
     * @param params post form 提交的参数
     * @return
     */
    public static String postFormParams(String url, Map<String, String> params,String vervy) {
        FormBody.Builder builder = new FormBody.Builder();
        //添加参数
        if (params != null && params.keySet().size() > 0) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
        }
        Request request = new Request.Builder()
                .addHeader("X-Authorization",vervy)
                .url(url)
                .post(builder.build())
                .build();
        return execNewCall(request);
    }


    /**
     * Post请求发送JSON数据....{"name":"zhangsan","pwd":"123456"}
     * 参数一：请求Url
     * 参数二：请求的JSON
     * 参数三：请求回调
     */
    public static String postJsonParams(String url, String jsonParams,String vervy) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonParams);
        Request request = new Request.Builder()
                .addHeader("X-Authorization",vervy)
                .url(url)
                .post(requestBody)
                .build();
        return execNewCall(request);
    }

    public static String postJsonParams1(String url, String jsonParams) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonParams);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        return execNewCall(request);
    }

    /**
     * Post请求发送xml数据....
     * 参数一：请求Url
     * 参数二：请求的xmlString
     * 参数三：请求回调
     */
    public static String postXmlParams(String url, String xml) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/xml; charset=utf-8"), xml);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        return execNewCall(request);
    }

    public static  String delete(String url, Map<String, String> queries,String vervy){
        StringBuffer sb = getQueryString(url,queries);
        Request request = new Request.Builder()
                .addHeader("X-Authorization",vervy)
                .delete()
                .url(sb.toString())
                .build();
        return execNewCall(request);
    }

    public static ResultInfo<JSONObject> bldPostJsonParams(String url, String jsonParams) {
        return bldPostJsonParams(url, jsonParams, null);
    }

    public static ResultInfo bldPostJsonParams(String url, HashMap<String, String> headerMap) {
        return bldPostJsonParams(url, "", headerMap);
    }

    public static ResultInfo<JSONObject> bldPostJsonParams(String url, String paramJ, HashMap<String, String> headerMap) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), paramJ);
        Request.Builder builder = new Request.Builder().url(url).post(requestBody);
        if (headerMap != null){
            for (Map.Entry<String, String> m : headerMap.entrySet()){
                builder.addHeader(m.getKey(), String.valueOf(m.getValue()));
            }
        }
        return bldCall(builder.build());
    }

    public static ResultInfo bldDeleteJsonParams(String url, String token) {
        Request request = new Request.Builder()
                .addHeader("X-Authorization", token)
                .url(url)
                .delete()
                .build();
        return bldCall(request);
    }

    private static ResultInfo<JSONObject> bldCall(Request request){
        Response response = null;
        try {
            OkHttpClient okHttpClient = SpringUtils.getBean(OkHttpClient.class);
            response = okHttpClient.newCall(request).execute();
            ResponseBody body = response.body();
            if (body != null){
                JSONObject j = JSONObject.parseObject(body.string());
                if (response.isSuccessful()){
                    return ResultInfo.success(j);
                }else {
                    String message = j.get("message").toString();
                    int code = Integer.parseInt(String.valueOf(j.get("status")));
                    return ResultInfo.error(code, message);
                }
            }
        } catch (Exception e) {
            logger.error("okhttp3 put error >> ex = {}", ExceptionUtils.getStackTrace(e));
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return ResultInfo.error("请求失败");
    }
}
