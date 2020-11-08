package com.bld.project.system.device;

import com.alibaba.fastjson.JSONObject;
import com.bld.common.utils.StringUtils;
import com.bld.common.utils.security.ShiroUtils;
import com.bld.framework.web.domain.ResultInfo;
import com.bld.project.bldEnums.DeviceAttributeTypeEnum;
import com.bld.project.system.device.model.TbDevice;
import com.bld.project.system.device.service.DeviceService;
import com.bld.project.system.user.domain.ThingsboardUser;
import com.bld.project.utils.DeviceQuery;
import com.bld.project.utils.ListQuery;
import com.bld.project.utils.TbApiUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.web3j.crypto.CipherException;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;

@Controller
@RequestMapping("device")
public class DeviceController {
    @Resource
    private DeviceService deviceService;

    @GetMapping("getDeviceManage.html")
    public String getDeviceManage(){
        return "system/cilent/device.html";
    }

    @RequestMapping("searchDeviceList.json")
    @ResponseBody
    public ResultInfo searchDeviceList(@RequestBody ListQuery query){
        System.out.println(query);
        return StringUtils.isNullString(query.getId()) ? deviceService.searchDevice(query.getLimit(), query.getSearch(), ShiroUtils.getTbToken(),query.getTextOffset(),query.getIdOffset()) : deviceService.getDeviceListByCustomer(query.getLimit(), query.getSearch(), query.getId());
    }

    @PostMapping("delDeviceDistribution.json")
    @ResponseBody
    public ResultInfo delDeviceDistribution(@RequestBody JSONObject json){
        Object deviceId = json.get("deviceId");
        if (StringUtils.isNullString(deviceId)){
            return ResultInfo.error("没有获取到您要取消分配的设备ID");
        }
        return deviceService.delDeviceDistribution(deviceId.toString());
    }

    @PostMapping("deviceDistribution.json")
    @ResponseBody
    public ResultInfo deviceDistribution(@RequestBody JSONObject json){
        Object deviceId = json.get("deviceId");
        Object customerId = json.get("customerId");
        if (StringUtils.isNullString(deviceId) || StringUtils.isNullString(customerId)){
            return ResultInfo.error("参数错误");
        }
        return deviceService.deviceDistribution(customerId.toString(), deviceId.toString());
    }

    @PostMapping("addDevice.json")
    @ResponseBody
    public ResultInfo addDevice(@RequestBody JSONObject json) throws CipherException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, IOException {
        Object name = json.get("name");
        Object type = json.get("type");
        if (StringUtils.isNullString(name)){
            return ResultInfo.error("请输入名字");
        }
        if (StringUtils.isNullString(type)){
            return ResultInfo.error("请输入设备类型");
        }
        String label = null;
        if (!StringUtils.isNullString(json.get("label"))){
            label = json.get("label").toString();
        }
        String description = null;
        if (!StringUtils.isNullString(json.get("description"))){
            description = json.get("description").toString();
        }
        String gateway = null;
        if (!StringUtils.isNullString(json.get("gateway"))){
            gateway = json.get("gateway").toString();
        }
        return deviceService.addDevice(name.toString(), type.toString(), label, description, gateway, ShiroUtils.getTbToken());
    }

    @PostMapping("updateDevice.json")
    @ResponseBody
    public ResultInfo updateDevice(@RequestBody JSONObject json){
        return deviceService.updateDevice(json);
    }

    @GetMapping("getDeviceTypes.json")
    @ResponseBody
    public ResultInfo getDeviceTypes(){
        return deviceService.getDeviceTypes();
    }

    @PostMapping("delDevice.json")
    @ResponseBody
    public ResultInfo delDevice(@RequestBody JSONObject json){
        Object deviceId = json.get("deviceId");
        if (StringUtils.isNullString(deviceId)){
            return ResultInfo.error("没有获取到您删除的设备ID");
        }
        return deviceService.delDevice(deviceId.toString());
    }

    @GetMapping("getDeviceToken.json")
    @ResponseBody
    public ResultInfo getDeviceToken(String deviceId){
        return deviceService.getDeviceToken(deviceId, ShiroUtils.getTbToken());
    }

    @RequestMapping("publicDevice.json")
    @ResponseBody
    public ResultInfo publicDevice(@RequestBody JSONObject json){
        Object id = json.get("deviceId");
        return StringUtils.isNullString(id) ? ResultInfo.error("没有获取到设备ID") : ResultInfo.success(deviceService.publicDevice(id.toString()));
    }

    @GetMapping("getDeviceAlarm.json")
    @ResponseBody
    public ResultInfo getDeviceAlarm(@RequestBody DeviceQuery query){
        String s = query.checkAlarm();
        return s != null ? ResultInfo.error(s) : deviceService.getDeviceAlarm(query.getId(), query.getLimit(), query.getStartTime(), query.getEndTime(), query.getSearchStatus(), query.isFetchOriginator(), query.isAscOrder());
    }

    @GetMapping("getDeviceEvent.json")
    @ResponseBody
    public ResultInfo getDeviceEvent(@RequestBody DeviceQuery query){
        String s = query.checkEvent();
        return s != null ? ResultInfo.error(s) : deviceService.getDeviceEvent(query.getId(), query.getType(), query.getTenantId(), query.getLimit(), query.getStartTime(), query.getEndTime());
    }

    @GetMapping("attributes.json")
    @ResponseBody
    public ResultInfo getAttributes(String id, String type){
        return StringUtils.isNullString(id) || StringUtils.isNullString(type) ? ResultInfo.error("参数错误") : deviceService.getDeviceAttributes(id, type);
    }

    @PostMapping("addDeviceAttribute.json")
    @ResponseBody
    public ResultInfo addDeviceAttribute(@RequestBody JSONObject json){
        String id = json.getString("id");
        String type = json.getString("type");
        String attribute = json.getString("attribute");
        return StringUtils.isNullString(id) || StringUtils.isNullString(type) || StringUtils.isNullString(attribute) ? ResultInfo.error("参数错误") : deviceService.addDeviceAttributes(id, type, attribute);
    }

    @PostMapping("delDeviceAttributes.json")
    @ResponseBody
    public ResultInfo delDeviceAttributes(@RequestBody JSONObject json){
        Object deviceId = json.get("deviceId");
        Object k = json.get("ks");
        if (StringUtils.isNullString(deviceId) || StringUtils.isNullString(k)){
            ResultInfo.error("参数错误");
        }
        List<String> list = JSONObject.parseObject(JSONObject.toJSONString(k), List.class);
        if (list == null || list.size() < 1){
            return ResultInfo.error("没有获取到待删除的属性名称");
        }
        int i = 0;
        for (String s : list){
            ResultInfo resultInfo = deviceService.delDeviceAttributes(deviceId.toString(), s);
            i = resultInfo.isSuccess() ? ++i : i;
        }
        return  i == 0 ? ResultInfo.error("删除失败" ) : ResultInfo.success(i == list.size() ? "删除成功" : "部分删除成功");
    }

    @GetMapping("searchMaintain.json")
    @ResponseBody
    public ResultInfo searchMaintain(String search, int pageSize, int pageNum){
        ThingsboardUser tbUser = ShiroUtils.getSysUser().getThingsboardUser();
        String cId = null;
        if (!tbUser.isTenantAdmin() && !tbUser.isSysAdmin()){
            cId = tbUser.getCustomerId().getId();
        }
        return deviceService.searchMaintain(search, cId, pageSize, pageNum);
    }


    @GetMapping("autoAttributes.json")
    @ResponseBody
    public void autoAttributes(){
        TbApiUtils tbApiUtils = new TbApiUtils();
        String token = tbApiUtils.loginTb();
        ResultInfo<List<TbDevice>> resultInfo = deviceService.searchDevice(1000, null, token,"","");
        if (resultInfo.isSuccess()){
            for (TbDevice td : resultInfo.getData()){
                String dId = td.getId().getId();
                JSONObject j = new JSONObject();
                j.put("ChargingFee", "{\"00:00-07:59\":\"0.50\",\"08:00-17:59\":\"1.20\",\"18:00-23:59\":\"0.80\"}");
                j.put("ChargingPileSwitch", "ON");
                j.put("ChipID", td.getTitle());
                j.put("Location", "{\"Lon\":\"123.00349\",\"Lat\":\"40.32343\"}");
                j.put("maintaining", false);
                j.put("manufacturer", "BLD");
                j.put("Operator", "GWJS");
                j.put("OrderlyCharging", "17:30-19:30");
                j.put("SPICAPACITY", 4);
                j.put("WorkingVersion", "2007071");
                ResultInfo resultInfo1 = deviceService.addDeviceAttributes(dId, DeviceAttributeTypeEnum.SHARED_SCOPE.getName(), j.toJSONString());
                System.out.println(resultInfo1);
            }
        }
    }
}
