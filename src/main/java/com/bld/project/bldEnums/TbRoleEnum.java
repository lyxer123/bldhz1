package com.bld.project.bldEnums;

/**
 * @author SOFAS
 * @date 2020/7/3
 * @directions thingsboard 角色
*/
public enum TbRoleEnum {
    /**
     * 系统管理员角色
     */
    SYS_ADMIN("SYS_ADMIN", "系统管理员"),
    TENANT_ADMIN("TENANT_ADMIN", "租户管理员"),
    CUSTOMER_USER("CUSTOMER_USER", "顾客用户");

    private String name;
    private String directions;

    public String getName() {
        return name;
    }

    public String getDirections() {
        return directions;
    }

    public static TbRoleEnum getMessage(String name){
        for(TbRoleEnum tbRoleEnum : TbRoleEnum.values()){
            if(tbRoleEnum.getName().equals(name)){
                return tbRoleEnum;
            }
        }
        return null;
    }

    TbRoleEnum(String name, String directions) {
        this.name = name;
        this.directions = directions;
    }
}
