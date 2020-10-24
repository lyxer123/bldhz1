package com.bld.project.system.device.model;

import com.alibaba.fastjson.JSONObject;
import com.bld.project.system.user.domain.TbId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author SOFAS
 * @date 2020/7/3
 * @directions thingsboard 设备实体
*/
@Data
public class TbDevice implements Serializable {
    private TbId id;
    /**
     * 租户id
     */
    private TbId tenantId;
    /**
     * 客户id
     */
    private TbId customerId;
    /**
     * 名称（显示）
     */
    private String name;
    /**
     * 名称（修改时使用）
     */
    private String title;
    /**
     * 设备类型
     */
    private String type;
    /**
     * 标签
     */
    private String label;
    /**
     * 备注
     */
    private JSONObject additionalInfo;
    /**
     * 添加时间
     */
    private Date createdTime;
}
