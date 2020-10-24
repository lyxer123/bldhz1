package com.bld.project.system.customer.domain;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.bld.project.system.user.domain.TbId;
import lombok.Data;

/**
 * @author SOFAS
 * @date 2020/5/8
 * @directions 租户下客户实体
*/
@Data
public class Customer {
    /**
     * 客户id
     */
    private TbId id;
    /**
     * 所属租户id
     */
    private TbId tenantId;
    /**
     * 名称
     */
    @JsonAlias("name")
    private String name;
    /**
     * 标题，提交修改时新的name, 添加时需要
     */
    private String title;
    /**
     * 邮箱, 添加时需要
     */
    @JsonAlias("email")
    private String email;
    /**
     * 电话, 添加时需要
     */
    @JsonAlias("phone")
    private String phone;
    /**
     * 邮编, 添加时需要
     */
    @JsonAlias("zip")
    private String zip;
    /**
     * 城市, 添加时需要
     */
    @JsonAlias("city")
    private String city;
    /**
     * 省, 添加时需要
     */
    @JsonAlias("state")
    private String state;
    /**
     * 国家, 添加时需要
     */
    @JsonAlias("country")
    private String country;
    /**
     * 地址1, 添加时需要
     */
    @JsonAlias("address")
    private String address;
    /**
     * 地址2, 添加时需要
     */
    @JsonAlias("address2")
    private String address2;
    /**
     * 创建时间
     */
    private long createdTime;
    /**
     * 附加信息，描述, 添加时需要
     */
    private JSONObject additionalInfo;
}
