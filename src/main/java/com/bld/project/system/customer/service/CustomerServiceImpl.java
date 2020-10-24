package com.bld.project.system.customer.service;

import com.alibaba.fastjson.JSONObject;
import com.bld.common.utils.StringUtils;
import com.bld.common.utils.security.ShiroUtils;
import com.bld.framework.aspectj.lang.annotation.TokenNotNull;
import com.bld.framework.utils.OkHttpUtil;
import com.bld.framework.web.domain.ResultInfo;
import com.bld.project.system.customer.domain.Customer;
import com.bld.project.utils.TbApiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

import static com.bld.project.utils.TbApiUtils.*;

@TokenNotNull
@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {
    @Override
    public ResultInfo addCustomer(Customer customer) {
        String title = customer.getTitle();
        String name = customer.getName();
        if (StringUtils.isNullString(title)){
            if (StringUtils.isNullString(name)){
                return ResultInfo.error("客户名称不能为空");
            }
            customer.setTitle(name);
        }
        customer.setId(null);
        customer.setTenantId(null);
        customer.setName(null);
        String url = saveCustomerApi();
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");
        headerMap.put("X-Authorization", ShiroUtils.getTbToken());
        return OkHttpUtil.bldPostJsonParams(url, JSONObject.toJSONString(customer), headerMap);
    }

    @Override
    public ResultInfo deleteCustomer(String userId) {
        String url = deleteCustomerApi(userId);
        ResultInfo resultInfo = OkHttpUtil.bldDeleteJsonParams(url, ShiroUtils.getSysUser().getAuthorization());
        return resultInfo.isSuccess() ? ResultInfo.success("删除成功") : ResultInfo.error("删除失败，请稍后再试");
    }

    @Override
    public ResultInfo updateCustomer(JSONObject json) {
        String url = saveCustomerApi();
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("X-Authorization", ShiroUtils.getSysUser().getAuthorization());
        headerMap.put("Content-Type", "application/json");
        return OkHttpUtil.bldPostJsonParams(url, json.toJSONString(), headerMap);
    }

    @Override
    public ResultInfo selectCustomer(Integer limit, String search) {
        String url = selectCustomerApi(limit, search);
        JSONObject j = TbApiUtils.get(url);
        Object data = j.get("data");
        return data == null ? ResultInfo.error("没有查询到数据") : ResultInfo.success(data);
    }

    @Override
    public ResultInfo<JSONObject> getCustomerName(List<String> ids) {
        JSONObject nameJ = new JSONObject();
        for (String id : ids){
            String url = getCustomerNameApi(id);
            JSONObject j = get(url);
            if (j != null && !StringUtils.isNullString(j.get("title"))){
                nameJ.put(id, j.get("title"));
            }
        }
        return ResultInfo.success(nameJ);
    }

    @Override
    public ResultInfo<JSONObject> getCustomerAttributes(String customerId) {
        ResultInfo<JSONObject> result = new ResultInfo<>();
        String url = TbApiUtils.getCustomerAttributes(customerId);
        ResultInfo<String> resultInfo = bldGet(url);
        if (resultInfo.isSuccess()){
            String data = resultInfo.getData();
            List<JSONObject> js = JSONObject.parseArray(data, JSONObject.class);
            JSONObject j = new JSONObject();
            for (JSONObject j1 : js){
                j.put(j1.getString("key"), j1.getString("value"));
            }
            result.setSuccess(true);
            result.setData(j);
            return result;
        }
        result.setMessage(resultInfo.getMessage());
        return result;
    }

    @Override
    public ResultInfo updateCustomerAttributes(String customerId, String tbToken, String param) {
        String url = TbApiUtils.updateCustomerAttributes(customerId);
        return OkHttpUtil.bldPostJsonParams(url, param, TbApiUtils.getHeader(tbToken));
    }
}
