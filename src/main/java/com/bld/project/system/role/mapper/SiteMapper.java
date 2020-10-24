package com.bld.project.system.role.mapper;

import com.bld.project.system.role.domain.Pileasset;
import com.bld.project.system.role.domain.Site;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface SiteMapper {
    //所有
    @Select("select id,site_name,adress,Gps,pileassets ,manager from site")
    public List<Site> findAll();
    //添加站点
    @Insert("insert site (site_name,Gps,adress,manager) value(#{siteName},#{gps},#{adress},#{manager})")
    public int saveSite(Site site);

    //关联设备
    @Update("update site s  set  s.pileasset=#{chipIds} where s.id=#{id}")
    public int relevancyDevcie(String chipIds , String id);

    @Delete("delete from  site s where s.id=#{id}")
    public int deleteSite(Integer id);

    @Select("select id,site_name,adress,Gps,pileassets ,manager from site s where s.id=#{id}")
    public Site siteById(Integer id);

    @Update("update site s  set  s.site_name=#{siteName},s.Gps=#{gps},s.adress=#{adress},s.manager=#{manager} where s.id=#{id}")
    public int updateSite(Site site);

}
