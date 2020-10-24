package com.bld.project.system.user.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author SOFAS
 * @date 2020/5/7
 * @directions  thingsboard id实体
*/
@Data
public class TbId implements Serializable {
    private String entityType;
    private String id;
}
