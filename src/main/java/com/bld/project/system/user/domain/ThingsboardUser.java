package com.bld.project.system.user.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.bld.common.utils.StringUtils;
import com.bld.project.bldEnums.TbRoleEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @author SOFAS
 * @date 2020/5/7
 * @directions  thingsboard用户实体
*/
@Data
public class ThingsboardUser implements Serializable {
    private static final long serialVersionUID = -8006548948291819141L;
    /**
     * 用户id
     */
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
     * 邮箱
     */
    private String email;
    /**
     * 权限
     */
    private String authority;
    /**
     * 名
     */
    private String firstName;
    /**
     * 姓
     */
    private String lastName;
    /**
     * 创建时间
     */
    private Long createdTime;
    /**
     * 附加信息
     */
//    additionalInfo
    private AdditionalInfo additionalInfo;
    /**
     * 是否发送激活邮件
     */
    @   JSONField(serialize = false)
    private boolean sendActivationMail;
    /*{"id":{"entityType":"USER","id":"07861800-2ae4-11ea-9ef5-6f9eb2c093ac"},"createdTime":1577696945536,"userAdditionalInfo":{"userPasswordHistory":{"1577696962994":"$2a$10$UBA30blRwdaLSGdGDMH/8eAqHQ03BX1zZ8ckO4JvJeuVsM98AaWJm","1585122771694":"$2a$10$FNclIM7.ALwSfa67ibxoY.mkk3s3WT7tNd22BXzGaGHIBoxTbf4gO","1585122819025":"$2a$10$24JXdg661f.Rywc.Mi80k.t2DlwodsqMF6GEbP0r1hm9hfim0aNn6","1585272093826":"$2a$10$tnL1XyDAujM6SOifY.Z3c.vxfmhg3cjbyXlNZyQqeAKhi7KRTF/CK","1585272570574":"$2a$10$zpeEb/6wgOZ5sFgE/tZbKe0OjTUyHaUL56c6sjpFXlOjz2HzKvQf6"},"lastLoginTs":1588830414492,"failedLoginAttempts":0,"lang":"zh_CN"},"tenantId":{"entityType":"TENANT","id":"dc668330-2ae3-11ea-9ef5-6f9eb2c093ac"},"customerId":{"entityType":"CUSTOMER","id":"13814000-1dd2-11b2-8080-808080808080"},"email":"1005750@qq.com","authority":"TENANT_ADMIN","firstName":"liu","lastName":"yongxiang","name":"1005750@qq.com"}*/

    public String addCheck(){
        if (StringUtils.isBlank(email)){
            return "邮箱不能为空";
        }
        return null;
    }

    public String updateCheck(){
        if (customerId == null && tenantId == null){
            return "没有获取到客户id，请重新添加";
        }
        return null;
    }

    public boolean isSysAdmin(){
        return TbRoleEnum.SYS_ADMIN.getName().equals(authority);
    }

    public boolean isTenantAdmin(){
        return TbRoleEnum.TENANT_ADMIN.getName().equals(authority);
    }

    public boolean isCustomer(){
        return TbRoleEnum.CUSTOMER_USER.getName().equals(authority);
    }
}
