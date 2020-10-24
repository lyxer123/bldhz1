package com.bld.project.system.user.domain;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

@Data
public class AdditionalInfo implements Serializable {
/*"userAdditionalInfo":{"userPasswordHistory":{"1577696962994":"$2a$10$UBA30blRwdaLSGdGDMH/8eAqHQ03BX1zZ8ckO4JvJeuVsM98AaWJm","1585122771694":"$2a$10$FNclIM7.ALwSfa67ibxoY.mkk3s3WT7tNd22BXzGaGHIBoxTbf4gO","1585122819025":"$2a$10$24JXdg661f.Rywc.Mi80k.t2DlwodsqMF6GEbP0r1hm9hfim0aNn6","1585272093826":"$2a$10$tnL1XyDAujM6SOifY.Z3c.vxfmhg3cjbyXlNZyQqeAKhi7KRTF/CK","1585272570574":"$2a$10$zpeEb/6wgOZ5sFgE/tZbKe0OjTUyHaUL56c6sjpFXlOjz2HzKvQf6"},"lastLoginTs":1588830414492,"failedLoginAttempts":0,"lang":"zh_CN"}*/
    /**
     * 使用语言
     */
    private String lang;
    /**
     * 说明信息
     */
    private String description;
    /**
     * 最后登录时间
     */
    private Long lastLoginTs;
    /**
     * 尝试登录失败的次数
     */
    private Integer failedLoginAttempts;
    /**
     * 用户历史密码记录
     */
    private JSONObject userPasswordHistory;
}
