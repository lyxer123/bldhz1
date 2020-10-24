package com.bld.project.system.customer.controller;

import com.alibaba.fastjson.JSONObject;
import com.bld.common.utils.StringUtils;
import com.bld.common.utils.security.ShiroUtils;
import com.bld.framework.web.controller.BaseController;
import com.bld.framework.web.domain.ResultInfo;
import com.bld.project.bldEnums.TbRoleEnum;
import com.bld.project.system.block.mapper.BlockDeviceMapper;
import com.bld.project.system.block.mapper.BlockHashMapper;
import com.bld.project.system.block.model.BlockDevice;
import com.bld.project.system.block.model.BlockHash;
import com.bld.project.system.customer.domain.Customer;
import com.bld.project.system.customer.service.CustomerService;
import com.bld.project.system.user.domain.TbId;
import com.bld.project.system.user.domain.ThingsboardUser;
import com.bld.project.utils.BlockUtils;
import com.bld.project.utils.ListQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.web3j.crypto.CipherException;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author SOFAS
 * @date 2020/5/7
 * @directions  客户处理接口
*/
@Slf4j
@Controller
@RequestMapping("/customer")
public class CustomerController extends BaseController {
    @Resource
    private CustomerService customerService;
    @Resource
    private BlockDeviceMapper blockDeviceMapper;
    @Resource
    private BlockHashMapper blockHashMapper;

    @GetMapping("/client.html")
    public String openClientHtml(){
        return "system/cilent/client.html";
    }

    @RequestMapping("addCustomer.json")
    @ResponseBody
    public ResultInfo addCustomer(@RequestBody JSONObject json){
        Customer customer = JSONObject.parseObject(json.toJSONString(), Customer.class);
        Object description = json.get("description");
        if (!StringUtils.isNullString(description)){
            JSONObject j = new JSONObject();
            j.put("description", description);
            customer.setAdditionalInfo(j);
        }
        return customerService.addCustomer(customer);
    }

    @RequestMapping("deleteCustomer.json")
    @ResponseBody
    public ResultInfo deleteCustomer(@RequestBody JSONObject json){
        Object userId = json.get("userId");
        if (StringUtils.isNullString(userId)){
            return ResultInfo.error("没有获取到您需要删除的客户的ID");
        }
        return customerService.deleteCustomer(userId.toString());
    }

    @RequestMapping("updateCustomer.json")
    @ResponseBody
    public ResultInfo updateCustomer(@RequestBody JSONObject json){
        return customerService.updateCustomer(json);
    }

    @RequestMapping("selectCustomer.json")
    @ResponseBody
    public ResultInfo selectCustomer(@RequestBody ListQuery query){
        return customerService.selectCustomer(query.getLimit(), query.getSearch());
    }

    @GetMapping("getCustomerName.json")
    @ResponseBody
    public ResultInfo getCustomerName(String ids){
        if (StringUtils.isNullString(ids)){
            return ResultInfo.error("参数错误");
        }
        return customerService.getCustomerName(Arrays.asList(ids.split(",")));
    }

    @GetMapping("getCustomerAttributes.json")
    @ResponseBody
    public ResultInfo getCustomerAttributes(String customerId, String search){
        ThingsboardUser tbUser = ShiroUtils.getSysUser().getThingsboardUser();
        if (tbUser.isCustomer()){
            customerId = tbUser.getCustomerId().getId();
        }
        ArrayList<JSONObject> list = new ArrayList<>();
        if (!StringUtils.isNullString(customerId)){
            ResultInfo<JSONObject> car = customerService.getCustomerAttributes(customerId);
            JSONObject ca = car.getData();
            ca.put("id", customerId);
            ArrayList<String> ids = new ArrayList<>();
            ids.add(customerId);
            ResultInfo<JSONObject> cnr = customerService.getCustomerName(ids);
            JSONObject nameJ = cnr.getData();
            String name = nameJ.getString(customerId);
            ca.put("name", name);
            String wallet = ca.getString("wallet");
            if (!StringUtils.isNullString(wallet)){
                long balance = BlockUtils.getBalance(wallet);
                ca.put("balance", balance);
            }
            list.add(ca);
            return ResultInfo.success(list);
        }
        ResultInfo resultInfo = customerService.selectCustomer(100, search);
        if (!resultInfo.isSuccess()){
            return resultInfo;
        }
        Object data = resultInfo.getData();
        List<JSONObject> jsons = JSONObject.parseArray(data.toString(), JSONObject.class);
        for (JSONObject j : jsons){
            JSONObject aj = new JSONObject();
            String ids = j.getString("id");
            TbId tbId = JSONObject.parseObject(ids, TbId.class);
            aj.put("id", tbId.getId());
            String title = j.getString("title");
            aj.put("name", title);
            ResultInfo<JSONObject> car = customerService.getCustomerAttributes(tbId.getId());
            if (car.isSuccess()){
                JSONObject dataj = car.getData();
                String wallet = dataj.getString("wallet");
                aj.put("wallet", wallet);
                if (!StringUtils.isNullString(wallet)){
                    long balance = BlockUtils.getBalance(wallet);
                    aj.put("balance", balance);
                }
            }
            list.add(aj);
        }
        return ResultInfo.success(list);
    }

    @PostMapping("updateCustomerAttributes.json")
    @ResponseBody
    public ResultInfo updateCustomerAttributes(@RequestBody JSONObject j){
        ThingsboardUser tbUser = ShiroUtils.getSysUser().getThingsboardUser();
        String id = j.getString("id");
        String attribute = j.getString("attribute");
        return TbRoleEnum.TENANT_ADMIN.getName().equals(tbUser.getAuthority()) ? customerService.updateCustomerAttributes(id, ShiroUtils.getTbToken(), attribute) : ResultInfo.error("您没有操作权限");
    }

    @GetMapping("blockCollect.json")
    @ResponseBody
    public ResultInfo getDeviceTypes(String id) throws IOException, CipherException {
        ThingsboardUser tbUser = ShiroUtils.getSysUser().getThingsboardUser();
        if (!tbUser.isTenantAdmin() && !tbUser.isSysAdmin()){
            id = tbUser.getCustomerId().getId();
        }
        if (StringUtils.isNullString(id)){
            return ResultInfo.error("参数错误");
        }
        /*检查是否正在归集*/
        String s =  BlockUtils.collectMap.get(id);
        if (s != null){
            return ResultInfo.error("正在归集，请勿重复操作");
        }
        BlockUtils.collectMap.put(id, "");
        /*开始归集操作*/
        ResultInfo<JSONObject> car = customerService.getCustomerAttributes(id);
        if (!car.isSuccess()){
            return car;
        }
        JSONObject dataj = car.getData();
        String toWallet = dataj.getString("wallet");

        BlockDevice selectBd = new BlockDevice();
        selectBd.setCustomerId(id);
        List<BlockDevice> select = blockDeviceMapper.select(selectBd);
        if (select == null || select.size() < 1){
            return ResultInfo.error("没有查询到需要归集的设备");
        }
        for (BlockDevice bd : select){
            Long dm = bd.getDeviceMoney();
            /*如果小于手续费则不进行归集*/
            BigInteger gas = BlockUtils.gas;
            if(dm < gas.longValue()){
                continue;
            }

            dm -= gas.longValue();
            BigInteger value = new BigInteger(dm.toString(), 10);
            JSONObject j = new JSONObject();
            j.put("type", "collect");
            ResultInfo<String> resultInfo = BlockUtils.blockTransaction(bd.getDeviceWallet(), toWallet, value, gas, new BigInteger("47b760", 16), j.toJSONString());
            if (resultInfo.isSuccess()){
                BlockDevice updateBd = new BlockDevice();
                updateBd.setDeviceMoney(0L);
                blockDeviceMapper.update(updateBd, new BlockDevice(bd.getChipId()));
                blockHashMapper.add(new BlockHash(bd.getChipId(), resultInfo.getData(), toWallet, bd.getDeviceWallet(), dm, null));
            }
        }
        BlockUtils.collectMap.remove(id);
        return ResultInfo.success("设备归集完成");
    }
}
