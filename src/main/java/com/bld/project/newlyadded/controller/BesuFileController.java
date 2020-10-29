package com.bld.project.newlyadded.controller;


import com.bld.framework.web.domain.ResultInfo;
import com.bld.project.newlyadded.entity.BesuFileEntity;
import com.bld.project.newlyadded.service.BesuFileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/besufile")
public class BesuFileController {

    @Resource
    private BesuFileService besuFileService;

    @GetMapping("list")
    public ResultInfo<List<BesuFileEntity>> list(){
        System.out.println("请求进来");
        return  new ResultInfo<List<BesuFileEntity>>(besuFileService.getlist(),200,"success",true);

    }
}
