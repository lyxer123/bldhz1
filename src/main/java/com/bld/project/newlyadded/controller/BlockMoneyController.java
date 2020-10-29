package com.bld.project.newlyadded.controller;


import com.bld.framework.web.domain.ResultListInfo;
import com.bld.project.newlyadded.service.BlockMoneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("blockMoney")
public class BlockMoneyController {

    @Autowired
    private BlockMoneyService blockMoneyService;

    @RequestMapping
    public ResultListInfo  selectBlockHash(String hashtext){
        return blockMoneyService.selectBlockHash(hashtext);
    }



}
