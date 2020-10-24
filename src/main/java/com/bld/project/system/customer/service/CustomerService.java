package com.bld.project.system.customer.service;

import com.alibaba.fastjson.JSONObject;
import com.bld.framework.web.domain.ResultInfo;
import com.bld.project.system.customer.domain.Customer;

import java.util.List;

/**
 * @author SOFAS
 * @date 2020/5/7
 * @directions  客户业务逻辑处理
*/
public interface CustomerService {
    /**
     * @author SOFAS
     * @date   2020/5/7
     * @directions  添加客户
     * @param customer  添加的客户信息
     * @return  com.bld.framework.web.domain.ResultInfo
     */
    ResultInfo addCustomer(Customer customer);
    /**
     * @author SOFAS
     * @date   2020/5/7
     * @directions  删除客户
     * @param userId  客户id
     * @return  com.bld.framework.web.domain.ResultInfo
     */
    ResultInfo deleteCustomer(String userId);
    /**
     * @author SOFAS
     * @date   2020/5/23
     * @directions  更新客户信息
     * @param json  客户信息
     * @return  com.bld.framework.web.domain.ResultInfo
     */
    ResultInfo updateCustomer(JSONObject json);
    /**
     * @author SOFAS
     * @date   2020/5/23
     * @directions  查询客户列表
     * @param limit  每页查询数量
     * @param search  查询条件
     * @return  com.bld.framework.web.domain.ResultInfo
     */
    ResultInfo selectCustomer(Integer limit, String search);

    ResultInfo<JSONObject> getCustomerName(List<String> ids);
    /**
     * @author SOFAS
     * @date   2020/7/10
     * @directions  获取账户上属性
     * @param customerId 客户id
     * @return  com.bld.framework.web.domain.ResultInfo
     */
    ResultInfo<JSONObject> getCustomerAttributes(String customerId);
    ResultInfo updateCustomerAttributes(String customerId, String tbToken, String param);
}
