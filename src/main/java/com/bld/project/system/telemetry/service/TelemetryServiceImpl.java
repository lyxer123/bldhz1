package com.bld.project.system.telemetry.service;

import com.alibaba.fastjson.JSONObject;
import com.bld.framework.utils.OkHttpUtil;
import com.bld.framework.web.domain.ResultInfo;
import com.bld.project.utils.TbApiUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.bld.project.utils.TbApiUtils.saveOrUpdateTelemetryApi;

@Service
public class TelemetryServiceImpl implements TelemetryService{

    @Override
    public ResultInfo saveOrUpdateTelemetry(Map<String, String> map) {
        String dt = map.get("deviceToken");
        if (StringUtils.isBlank(dt)){
            return ResultInfo.error("设备通行证不能为空");
        }
        map.remove("deviceToken");
        String url = saveOrUpdateTelemetryApi(dt);
        JSONObject paramJ = new JSONObject();
        paramJ.putAll(map);
        return OkHttpUtil.bldPostJsonParams(url, paramJ.toJSONString());
    }

    @Override
    public ResultInfo<JSONObject> getTelemetrys(String keys, String id) {
        String url = TbApiUtils.getTelemetrysApi(keys, id, 0, System.currentTimeMillis());
        JSONObject j = TbApiUtils.get(url);
        System.out.println(j);
        return ResultInfo.ret(j);
    }
}
