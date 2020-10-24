package com.bld.project.system.role.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bld.framework.utils.OkHttpUtil;
import com.bld.framework.web.controller.BaseController;
import com.bld.framework.web.domain.AjaxResult;
import com.bld.framework.web.page.TableDataInfo;
import com.bld.project.system.role.domain.Device;
import com.bld.project.system.role.domain.Pileasset;
import com.bld.project.system.role.domain.Site;
import com.bld.project.system.role.service.IRoleService;
import com.bld.project.system.user.domain.User;
import com.bld.project.system.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("system/site")
public class SiteController extends BaseController {

    private String prefix = "system/cilent";

    @Value("${app.url}")
    private String URL;


    @GetMapping()
    public String site()
    {
        return  prefix+ "/site";
    }


    @GetMapping("/add")
    public String add()
    {
        return  prefix+ "/add";
    }
    @GetMapping("/relevancy")
    public String relevancy()
    {
        return  prefix+ "/relevancy";
    }


    @Autowired
    private IRoleService roleService;


    /**
     * 站点列表
     * @param site
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo siteList(Site site){
        startPage();
        List<Site> siteList = roleService.findAll();
        TableDataInfo dataTable = getDataTable(siteList);
        return dataTable;
    }

    /**
     * 新增站点
     * @param site
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult save(Site site){
        int i = roleService.saveSite(site);
        return toAjax(i);
    }




    /**
     * 批量刪除
     * @param ids
     * @return
     */
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        String[] idss = ids.split(",");
        for (String s : idss) {
            Integer integer = Integer.valueOf(s);
            int i = roleService.deleteSite(integer);
        }
        return toAjax(1);
    }

    /**
     * 修改站点属性
     * @param id
     * @param mmap
     * @return
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        Site site = roleService.siteById(id);
        mmap.put("site",site);
        return prefix + "/edit";
    }


    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave( Site site){
        int i = roleService.updateSite(site);
        return toAjax(i);
    }

    /**
     * 关联设备列表
     * @param id
     * @param mmap
     * @return
     */
    @PostMapping("/deviceList")
    @ResponseBody
    public String deviceList( @RequestParam Integer id, ModelMap mmap){
        List<Pileasset> pileassets=new ArrayList<>();
        Site site = roleService.siteById(id);
        String chipIds = site.getPileassets();
        if (chipIds == null || chipIds.equals("")){
            mmap.put("error","没有关联设备");
        }else{
            String[] deviceIds = chipIds.trim().split(",");
            for (int i = 0; i < deviceIds.length; i++) {
                Pileasset pileasset = roleService.pileassetbyChipId(deviceIds[i]);
                pileasset.setBlockData("");
                pileassets.add(pileasset);
            }
            mmap.put("id",id);
            mmap.put("list",pileassets);
        }

        return prefix + "/list";
    }

    /**
     * 取消关联设备
     * @param chipId
     * @param id
     * @return
     */
    @PostMapping("/cancel")
    @ResponseBody
    public AjaxResult cancel(@RequestParam String chipId,@RequestParam Integer id){
        Site site = roleService.siteById(id);
        String pileassets = site.getPileassets();
        String[] chipIds = pileassets.split(",");
        List<String> strings = Arrays.asList(chipIds);
        if (strings.contains(chipId)){
            strings.remove(chipId);
        }
        System.out.println(strings);
        return toAjax(1);
    }
    /**
     * 关联设备
     * @param chipIds
     * @param siteId
     * @return
     */
    @PostMapping("/relevancy/{id}")
    @ResponseBody
    public AjaxResult relevancy(String chipIds,@PathVariable("id")String siteId){
        int i = roleService.relevancyDevcie(chipIds,siteId);
        return toAjax(i);
    }


    }
