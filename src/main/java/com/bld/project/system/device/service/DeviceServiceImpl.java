package com.bld.project.system.device.service;

import com.alibaba.fastjson.JSONObject;
import com.bld.project.newlyadded.untils.TextPageLink;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.bld.common.utils.security.ShiroUtils;
import com.bld.framework.utils.OkHttpUtil;
import com.bld.framework.web.domain.ResultInfo;
import com.bld.framework.web.domain.ResultListInfo;
import com.bld.project.bldEnums.DeviceAttributeTypeEnum;
import com.bld.project.system.block.mapper.BlockDeviceMapper;
import com.bld.project.system.block.model.BlockDevice;
import com.bld.project.system.block.service.BlockDeviceService;
import com.bld.project.system.device.model.TbDevice;
import com.bld.project.utils.TbApiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.web3j.crypto.CipherException;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class DeviceServiceImpl implements DeviceService{

    @Resource
    private BlockDeviceService blockDeviceService;
    @Resource
    private BlockDeviceMapper blockDeviceMapper;

    @Override
    public ResultInfo<List<TbDevice>> searchDevice(int limit, String search, String tbToken,String textOffset,String  idOffset) {
       // String url = TbApiUtils.searchDeviceApi(limit, search);
        String url = "http://125.64.98.21:8088/api/tenant/devices?limit="+ limit + (search == null ? "" : "&textSearch=" + search)+"&idOffset="+idOffset+"&textOffset="+textOffset;
        System.out.println("发送请求"+url);
        JSONObject j;
        ResultInfo<String> resultInfo = TbApiUtils.bldGet(url, tbToken);

        System.out.println(resultInfo);
        if (resultInfo.isSuccess()){
            j = JSONObject.parseObject(resultInfo.getData());
            List<TbDevice> list = JSONObject.parseArray(j.getString("data"), TbDevice.class);
            TextPageLink nextPageLink = JSONObject.parseObject(j.getString("nextPageLink"), TextPageLink.class);
            System.out.println(list);
            return ResultInfo.success(list, tbToken.substring(7),nextPageLink==null?"":nextPageLink.getTextOffset(),nextPageLink==null?"":nextPageLink.getIdOffset().toString());
        }
        return ResultInfo.error("没有查询到数据");
    }

    @Override
    public ResultInfo getDeviceListByCustomer(int limit, String search, String id) {
        String url = TbApiUtils.getDeviceListByCustomerIdApi(limit, search, id);
        JSONObject j = TbApiUtils.get(url);
        Object data = j.get("data");
        //System.out.println(data);
        return data != null ? ResultInfo.success(data) : ResultInfo.error("没有查询到数据");
    }

    @Override
    public ResultInfo delDeviceDistribution(String deviceId){
        String url = TbApiUtils.delDeviceDistributionApi(deviceId);
        ResultInfo resultInfo = OkHttpUtil.bldDeleteJsonParams(url, ShiroUtils.getTbToken());
        updateBlockDevice(resultInfo);
        return resultInfo;
    }

    @Override
    public ResultInfo deviceDistribution(String customerId, String deviceId) {
        String url = TbApiUtils.deviceDistributionApi(customerId, deviceId);
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("X-Authorization", ShiroUtils.getTbToken());
        ResultInfo resultInfo = OkHttpUtil.bldPostJsonParams(url, headerMap);
        updateBlockDevice(resultInfo);
        return resultInfo;
    }

    @Override
    public ResultInfo addDevice(String name, String type, String label, String description, String gateway, String tbToken) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchProviderException, CipherException, IOException {
        String url = TbApiUtils.addDeviceApi();
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("X-Authorization", tbToken);
        JSONObject param = new JSONObject();
        param.put("name", name);
        param.put("type", type);
        param.put("label", label);
        boolean f = description != null;
        boolean f1 = gateway != null;
        if (f || f1){
            JSONObject json = new JSONObject();
            if (f){
                json.put("description", description);
            }
            if (f1){
                json.put("gateway", gateway);
            }
            param.put("additionalInfo", json);
        }
        ResultInfo resultInfo = OkHttpUtil.bldPostJsonParams(url, param.toJSONString(), headerMap);
        if (resultInfo.isSuccess()){
            String s = resultInfo.getData().toString();
            TbDevice tbDevice = JSONObject.parseObject(s, TbDevice.class);
            blockDeviceService.asyncAddBlockDevice(new BlockDevice(tbDevice.getId().getId(), tbDevice.getTenantId().getId(), tbDevice.getCustomerId().getId(), tbDevice.getName()));
        }
        return resultInfo;
    }

    @Override
    public ResultInfo autoAddDevice(String name, String type, String tbToken, String workingVersion, String sn1, String sn2, boolean isBesu) throws NoSuchProviderException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, CipherException, IOException {
        String url = TbApiUtils.addDeviceApi();
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("X-Authorization", tbToken);
        JSONObject param = new JSONObject();
        param.put("name", name);
        param.put("type", type);
        ResultInfo resultInfo = OkHttpUtil.bldPostJsonParams(url, param.toJSONString(), headerMap);
        String deviceToken = null;
        if (resultInfo.isSuccess()){
            String s = resultInfo.getData().toString();
            TbDevice tbDevice = JSONObject.parseObject(s, TbDevice.class);
            String deviceId = tbDevice.getId().getId();
            ResultInfo<String> resultInfo1 = getDeviceToken(deviceId, tbToken);
            deviceToken = resultInfo1.isSuccess() ? resultInfo1.getData() : deviceToken;
            return blockDeviceService.addBlockDevice(new BlockDevice(deviceId, tbDevice.getTenantId().getId(), tbDevice.getCustomerId().getId(), tbDevice.getName(), deviceToken, workingVersion, sn1, sn2, isBesu), tbToken);
        }else if (resultInfo.getMessage().contains("org.hibernate.exception.ConstraintViolationException")){
            ResultInfo search = searchDevice(50, name, tbToken,"","");
            if (search.isSuccess()){
                List list = JSONObject.parseObject(JSONObject.toJSONString(search.getData()), List.class);
                Object o = list.get(0);
                TbDevice tbDevice = JSONObject.parseObject(JSONObject.toJSONString(o), TbDevice.class);
                String deviceId = tbDevice.getId().getId();
                ResultInfo<String> resultInfo1 = getDeviceToken(deviceId, tbToken);
                deviceToken = resultInfo1.isSuccess() ? resultInfo1.getData() : deviceToken;
                return blockDeviceService.addBlockDevice(new BlockDevice(deviceId, tbDevice.getTenantId().getId(), tbDevice.getCustomerId().getId(), tbDevice.getName(), deviceToken, workingVersion, sn1, sn2, isBesu), tbToken);
            }
        }
        return ResultInfo.error("设备添加失败");
    }

    @Override
    public ResultInfo updateDevice(JSONObject json) {
        String url = TbApiUtils.addDeviceApi();
        return OkHttpUtil.bldPostJsonParams(url, json.toJSONString(), TbApiUtils.getHeader(ShiroUtils.getTbToken()));
    }

    @Override
    public ResultInfo getDeviceTypes() {
        String url = TbApiUtils.getDeviceTypesApi();
        List list = TbApiUtils.getList(url, null);
        return ResultInfo.success(list);
    }

    @Override
    public ResultInfo delDevice(String deviceId) {
        String url = TbApiUtils.delDevice(deviceId);
        ResultInfo resultInfo = OkHttpUtil.bldDeleteJsonParams(url, ShiroUtils.getTbToken());
        if (resultInfo.isSuccess()){
            BlockDevice bd = new BlockDevice();
            bd.setDeviceId(deviceId);
            blockDeviceMapper.delete(bd);
        }
        return resultInfo;
    }

    @Override
    public ResultInfo<String> getDeviceToken(String deviceId, String token) {
        String url = TbApiUtils.getDeviceTokenApi(deviceId);
        ResultInfo<String> resultInfo = TbApiUtils.bldGet(url, token);
        if (resultInfo.isSuccess()){
            JSONObject json = JSONObject.parseObject(resultInfo.getData());
            String deviceToken = json.getString("credentialsId");
            resultInfo.setData(deviceToken);
        }
        return resultInfo;
    }

    @Override
    public ResultInfo publicDevice(String deviceId) {
        String url = TbApiUtils.publicDeviceApi(deviceId);
        ResultInfo resultInfo = OkHttpUtil.bldPostJsonParams(url, TbApiUtils.getHeader(ShiroUtils.getTbToken()));
        updateBlockDevice(resultInfo);
        return resultInfo;
    }

    @Override
    public ResultInfo getDeviceAlarm(String deviceId, int limit, long startTime, long endTime, String searchStatus, boolean fetchOriginator, boolean ascOrder) {
        String url = TbApiUtils.getDeviceAlarmApi(deviceId, limit, startTime, endTime, searchStatus, fetchOriginator, ascOrder);
        JSONObject json = TbApiUtils.get(url);
        return ResultInfo.ret(json.get("data"));
    }

    @Override
    public ResultInfo getDeviceEvent(String deviceId, String type, String tenantId, int limit, long startTime, long endTime) {
        String url = TbApiUtils.getDeviceAlarmApi(deviceId, type, tenantId, limit, startTime, endTime);
        JSONObject json = TbApiUtils.get(url);
        return ResultInfo.ret(json.get("data"));
    }

    @Override
    public ResultInfo<List<JSONObject>> getDeviceAttributes(String deviceId, String type) {
        String url = TbApiUtils.getDeviceAttributesApi(deviceId, type);
        return ResultInfo.ret(TbApiUtils.getList(url, null));
    }

    @Override
    public ResultInfo addDeviceAttributes(String deviceId, String type, String attribute) {
        String url = TbApiUtils.addDeviceAttributesApi(deviceId, type);
        return OkHttpUtil.bldPostJsonParams(url, attribute, TbApiUtils.getHeader(ShiroUtils.getTbToken()));
    }

    @Override
    public ResultInfo delDeviceAttributes(String deviceId, String k) {
        String url = TbApiUtils.delDeviceAttributesApi(deviceId, k);
        return OkHttpUtil.bldDeleteJsonParams(url, ShiroUtils.getTbToken());
    }

    @Override
    public ResultInfo searchMaintain(String search, String cId, int pageSize, int pageNum) {
        BlockDevice selectBd = new BlockDevice();
        selectBd.setSearch(search);
        selectBd.setCustomerId(cId);
        ArrayList<JSONObject> list = new ArrayList<>();
        Page<Object> page = PageHelper.offsetPage(pageNum, pageSize);
        List<BlockDevice> ds = blockDeviceMapper.select(selectBd);
        if (ds !=null){
            for (BlockDevice d : ds) {
                JSONObject json = new JSONObject();
                String dId = d.getDeviceId();
                json.put("deviceId", dId);
                json.put("deviceName", d.getChipId());
                ResultInfo<List<JSONObject>> da = getDeviceAttributes(dId, DeviceAttributeTypeEnum.SHARED_SCOPE.getName());
                if (da.isSuccess() && da.getData() != null) {
                    for (JSONObject j : da.getData()){
                        json.put(j.getString("key"), j.getString("value"));
                    }
                }
                list.add(json);
            }
        }
        return ResultListInfo.success(list, "", page.getTotal(), pageNum, pageSize);
    }

    /**
     * @author SOFAS
     * @date   2020/7/3
     * @directions  修改区块链设备信息
     * @param resultInfo  参数
     * @return  void
     */
    private void updateBlockDevice(ResultInfo resultInfo){
        if (resultInfo.isSuccess()){
            String s = resultInfo.getData().toString();
            TbDevice tbDevice = JSONObject.parseObject(s, TbDevice.class);
            BlockDevice updateBd = new BlockDevice(null, null, tbDevice.getCustomerId().getId(), null);
            BlockDevice whereBd = new BlockDevice(tbDevice.getId().getId(), null, tbDevice.getCustomerId().getId(), null);
            blockDeviceService.asyncUpdateBlockDevice(updateBd, whereBd);
        }
    }
}
