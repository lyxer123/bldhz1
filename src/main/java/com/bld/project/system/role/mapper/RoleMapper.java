package com.bld.project.system.role.mapper;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.bld.project.system.role.domain.*;
import com.bld.project.system.role.service.UpdatePileasset;
import org.apache.ibatis.annotations.*;

/**
 * 角色表 数据层
 * 
 * @author bld
 */
@Mapper
public interface RoleMapper
{
    /**
     * 根据条件分页查询角色数据
     * 
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    public List<Role> selectRoleList(Role role);

    /**
     * 根据用户ID查询角色
     * 
     * @param userId 用户ID
     * @return 角色列表
     */
    public List<Role> selectRolesByUserId(Long userId);

    /**
     * 通过角色ID查询角色
     * 
     * @param roleId 角色ID
     * @return 角色对象信息
     */
    public Role selectRoleById(Long roleId);

    /**
     * 通过角色ID删除角色
     * 
     * @param roleId 角色ID
     * @return 结果
     */
    public int deleteRoleById(Long roleId);

    /**
     * 批量角色用户信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteRoleByIds(Long[] ids);

    /**
     * 修改角色信息
     * 
     * @param role 角色信息
     * @return 结果
     */
    public int updateRole(Role role);

    /**
     * 新增角色信息
     * 
     * @param role 角色信息
     * @return 结果
     */
    public int insertRole(Role role);

    /**
     * 校验角色名称是否唯一
     * 
     * @param roleName 角色名称
     * @return 角色信息
     */
    public Role checkRoleNameUnique(String roleName);
    
    /**
     * 校验角色权限是否唯一
     * 
     * @param roleKey 角色权限
     * @return 角色信息
     */
    public Role checkRoleKeyUnique(String roleKey);

    @Select("select id ChipID,SPICAPACITY,WorkingVersion,UpdateVersion,CCID,IMEI,SN1,SN2,SD,ToWallet,FromWallet,Token,TIME,GPS,PD,OP,SP,Customer,MultiMeter from  pileassets  where Token =#{Token} ")
    public Pileassets selectByToken(String Token);


    @Insert("insert into pileasset (ChipID,SPICAPACITY,WorkingVersion,UpdateVersion,CCID,IMEI,SN1,SN2," +
            "SD,ToWallet,FromWallet,Token,TIME,GPS,PD,OP,SP,Customer,MultiMeter,DeviceID,peerUrl)value(#{chipId},#{spicapacity},#{workingVersion},#{updateVersion}," +
            "#{ccid},#{imei},#{sn1},#{sn2},#{sd},#{toWallet},#{fromWallet},#{token},#{time},#{gps},#{pd},#{op},#{sp},#{customer},#{multiMeter},#{deviceId},#{peerUrl})")
    public int save(Pileasset pt);

    @Select("select id,ChipID,SPICAPACITY,WorkingVersion,UpdateVersion,CCID,IMEI,SN1,SN2," +
            "SD,ToWallet,FromWallet,Token,TIME,GPS,PD,OP,SP,Customer,MultiMeter ,DeviceID,peerUrl from pileasset where pileasset.ChipID=#{ChipID}")
    public Pileasset pileassetbyChipId(String ChipID);

    @Update("update pileasset as p set p.SPICAPACITY=#{spicapacity},p.WorkingVersion=#{workingVersion},p.SN1=#{sn1},p.SN2=#{sn2}," +
            "p.IMEI=#{imei},p.CCID=#{ccid},p.ToWallet=#{toWallet} ,p.FromWallet=#{fromWallet},p.Token=#{token} ,p.peerUrl=#{peerUrl} , p.DeviceID=#{deviceId} where p.ChipID=#{chipId}")
    public int updatebyChipId(Pileasset  pileasset);

    @Update("update pileasset as p set p.SPICAPACITY=#{spicapacity},p.WorkingVersion=#{workingVersion},p.SN1=#{sn1},p.SN2=#{sn2}," +
            "p.IMEI=#{imei},p.CCID=#{ccid}, p.SD=#{sd},p.GPS=#{gps},p.PD=#{pd},p.OP=#{op},p.SP=#{sp},p.Customer=#{customer},p.MultiMeter=#{multiMeter} where p.ChipID=#{chipId}")
    public int updateDevice(Pileasset  pileasset);

    @Update("update pileasset as p set p.Token=#{token} ,p.FromWallet=#{wallet},p.DeviceID=#{deviceId} ,p.peerUrl=#{peerUrl}where p.ChipID=#{chipId}")
    public int updateToken(String  token,String wallet,String deviceId,String peerUrl,String chipId);


    @Select("select ID ,UpdateVersion,json from ota")
    public List<Ota>  versionAll();

    @Insert("insert into pilestatus (SN, UA, UB, UC, IA, IB, IC, PA, PB, PC, PF, GF, PT, EN, RSSI,ChipID)value(#{sn},#{ua},#{ub},#{uc},#{ia},#{ib},#{ic},#{pa},#{pb},#{pc},#{pf},#{gf},#{pt},#{en},#{rssi},#{chipId})")
    public int statusAdd(Pilestatus pilestatus);

    @Update("update pileasset as p set  p.FromWallet=${fromWallet} p.ToWallet=${toWallet},p.SN1=#{sn1},p.SN2=#{sn2},p.SD=#{sd},p.GPS=#{gps},p.PD=#{pd},p.OP=#{op},p.WorkingVersion=#{workingVersion} where p.ChipID=#{chipId}")
    public int updatePileasset(Pileasset pileasset);

    @Select("select json from ota o INNER join pileasset p on o.UpdateVersion > p.UpdateVersion  where p.ChipID = #{chipId}  and  o.UpdateVersion > #{version}")
    public String upgradeVersion(String chipId , String version);

    @Delete("delete  from pileasset  where DeviceID=#{deviceId}")
    public int delectDevice(String deviceId);

    @Select("select id ,ChipID,SPICAPACITY,WorkingVersion,UpdateVersion,CCID,IMEI,SN1,SN2," +
            "SD,ToWallet,FromWallet,Token,TIME,GPS,PD,OP,SP,Customer,MultiMeter ,DeviceID,peerUrl  from pileasset")
    public List<Pileasset> listPileasset();

    @Update("update pileasset as p set  p.FromWallet=${fromWallet} ,p.SN1=#{sn1},p.SN2=#{sn2},p.SD=#{sd},p.GPS=#{gps},p.PD=#{pd},p.OP=#{op},p.WorkingVersion=#{workingVersion} where p.ChipID=#{chipId}")
    public int editPileasset(Pileasset pileasset);

    @Update("update pileasset as p set p.Token=#{token} where p.DeviceID=#{deviceId}")
    public int updateToken1(String  token, String deviceId);

    @UpdateProvider(method = "autoUpdatePileasset1" ,type = UpdatePileasset.class)
    public Integer autoUpdatePileasset(@Param("pileasset") Pileasset pileasset)throws Exception;
}
