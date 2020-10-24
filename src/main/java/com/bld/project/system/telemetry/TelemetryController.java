package com.bld.project.system.telemetry;

import com.bld.framework.web.domain.ResultInfo;
import com.bld.project.system.telemetry.service.TelemetryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("telemetry")
public class TelemetryController {
    @Resource
    private TelemetryService telemetryService;

    @PostMapping("saveOrUpdateTelemetry.json")
    @ResponseBody
    public ResultInfo saveOrUpdateTelemetry(@RequestBody Map<String, String> map){
        return telemetryService.saveOrUpdateTelemetry(map);
    }

    @GetMapping("getTelemetrys.json")
    @ResponseBody
    public ResultInfo getTelemetrys(String keys, String deviceId){
        return telemetryService.getTelemetrys(keys, deviceId);
    }
}
