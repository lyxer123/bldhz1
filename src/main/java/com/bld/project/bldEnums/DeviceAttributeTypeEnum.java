package com.bld.project.bldEnums;

/**
 * @author SOFAS
 * @date 2020/7/8
 * @directions  设备属性类型枚举
*/
public enum DeviceAttributeTypeEnum {
    /**
     * 客户端属性
     */
    USER_SCOPE("USER_SCOPE", "客户端属性"),
    SERVER_SCOPE("SERVER_SCOPE", "服务端属性"),
    SHARED_SCOPE("SHARED_SCOPE", "共享属性");

    private String name;
    private String directions;

    public String getName() {
        return name;
    }

    public String getDirections() {
        return directions;
    }

    public static DeviceAttributeTypeEnum getMessage(String name){
        for(DeviceAttributeTypeEnum tbRoleEnum : DeviceAttributeTypeEnum.values()){
            if(tbRoleEnum.getName().equals(name)){
                return tbRoleEnum;
            }
        }
        return null;
    }

    DeviceAttributeTypeEnum(String name, String directions) {
        this.name = name;
        this.directions = directions;
    }
}
