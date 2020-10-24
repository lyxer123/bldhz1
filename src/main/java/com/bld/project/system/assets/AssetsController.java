package com.bld.project.system.assets;

import com.alibaba.fastjson.JSONObject;
import com.bld.common.utils.StringUtils;
import com.bld.framework.web.controller.BaseController;
import com.bld.framework.web.domain.ResultInfo;
import com.bld.project.system.assets.service.AssetsService;
import com.bld.project.utils.ListQuery;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author SOFAS
 * @date 2020/5/27
 * @directions  资产管理
*/
@Controller
@RequestMapping("assetsManage")
public class AssetsController extends BaseController {
    @Resource
    private AssetsService assetsService;

    @GetMapping("assetsManage.html")
    public String assetsManage(){
        return "system/cilent/assets.html";
    }

    @RequestMapping("assetsList.json")
    @ResponseBody
    public ResultInfo assetsList(@RequestBody ListQuery query){
        return StringUtils.isNullString(query.getId()) ? assetsService.assetsList(query.getLimit(), query.getSearch()) : assetsService.clientAssetsList(query.getLimit(), query.getSearch(), query.getId());
    }

    @RequestMapping("getAssetsTypes.json")
    @ResponseBody
    public ResultInfo getAssetsTypes(){
        return assetsService.getAssetsTypes();
    }

    @PostMapping("addAsset.json")
    @ResponseBody
    public ResultInfo addAsset(@RequestBody JSONObject json){
        if (StringUtils.isNullString(json.get("name"))){
            return ResultInfo.error("没有获取到您添加的资产的名称");
        }
        if (StringUtils.isNullString(json.get("type"))){
            return ResultInfo.error("没有获取到您添加的资产的类型");
        }
        return assetsService.addAsset(json);
    }

    @PostMapping("delAsset.json")
    @ResponseBody
    public ResultInfo delAsset(@RequestBody JSONObject json){
        Object assetId = json.get("assetId");
        return StringUtils.isNullString(assetId) ? ResultInfo.error("没有获取到您需要删除的资产id") : assetsService.delAsset(assetId.toString());
    }

    @PostMapping("updateAsset.json")
    @ResponseBody
    public ResultInfo updateAsset(@RequestBody JSONObject json){
        return assetsService.updateAsset(json);
    }

    @PostMapping("distributionAssets.json")
    @ResponseBody
    public ResultInfo distributionAssets(@RequestBody JSONObject json){
        Object assetsId = json.get("assetsId");
        Object customerId = json.get("customerId");
        return StringUtils.isNullString(assetsId) || StringUtils.isNullString(customerId) ? ResultInfo.error("您输入的参数不正确，请检查后再次尝试") : assetsService.distributionAssets(assetsId.toString(), customerId.toString());
    }

    @PostMapping("delDistribution.json")
    @ResponseBody
    public ResultInfo delDistribution(@RequestBody JSONObject json){
        Object assetsId = json.get("assetsId");
        return StringUtils.isNullString(assetsId) ? ResultInfo.error("没有获取到资产id") : assetsService.delDistribution(assetsId.toString());
    }

    @PostMapping("publicAsset.json")
    @ResponseBody
    public ResultInfo publicAsset(@RequestBody JSONObject json){
        Object assetId = json.get("assetId");
        return StringUtils.isNullString(assetId) ? ResultInfo.error("没有获取到设备ID") : assetsService.publicAsset(assetId.toString());
    }
}
