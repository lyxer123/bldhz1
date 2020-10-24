package com.bld.project.system.telemetry.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bld.framework.web.domain.ResultInfo;

import java.util.Map;

/**
 * @author SOFAS
 * @date 2020/4/27
 * @directions  遥测
*/
public interface TelemetryService {
    /**
     * @author SOFAS
     * @date   2020/4/27
     * @directions  保存或更新遥测数据
     * @param map   参数
     * @return  com.bld.framework.web.domain.ResultInfo
     */
    ResultInfo saveOrUpdateTelemetry(Map<String, String> map);
    ResultInfo<JSONObject> getTelemetrys(String keys, String id);
}
