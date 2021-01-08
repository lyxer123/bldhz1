package com.bld.project;

import com.alibaba.fastjson.JSONObject;
import com.bld.common.util.RedisUtil;
import com.bld.framework.web.domain.ResultInfo;
import com.bld.project.system.block.model.Block;
import com.bld.project.utils.BlockUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.web3j.crypto.CipherException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * @Author huangzhi
 * @Description
 * @Date 9:23 2021/1/8
 * @Param
 * @return
 **/
@Component
public class Test implements ApplicationRunner {


    @Autowired
    private RedisUtil redisUtil;

    private static Test factory;

    @PostConstruct
    public void init() {
        factory = this;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ceshi();
    }

    public static void ceshi() throws IOException, CipherException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
//        long wallet = BlockUtils.getBalance("c58b6da45db9486d33a87ed2fbb9fabe64436ef4");
//        System.out.println(wallet);
//        long wallet1 = BlockUtils.getBalance("cd4bbc34d0ca822665a98597f378b114667ce599");
//        System.out.println(wallet1);
//        long wallet2 = BlockUtils.getBalance("7cbe8fd74ade9f4cfdac85cce5960089116f3479");
//        System.out.println(wallet2);
        long wallet3 = BlockUtils.getBalance("3e1d94b47867010e37c078f69326e1f1ce5417b2");
        System.out.println("旷工的钱"+wallet3);


        JSONObject j = new JSONObject();
        j.put("type", "collect");
        ResultInfo<String> br = BlockUtils.blockTransaction("3e1d94b47867010e37c078f69326e1f1ce5417b2", "c58b6da45db9486d33a87ed2fbb9fabe64436ef4", new BigInteger("10000", 10), BlockUtils.gas, BlockUtils.gas_limit, j.toString());
        System.out.println(br);
//        ResultInfo<String> br1 = BlockUtils.blockTransaction("3e1d94b47867010e37c078f69326e1f1ce5417b2", "cd4bbc34d0ca822665a98597f378b114667ce599", new BigInteger("10000", 10), BlockUtils.gas, BlockUtils.gas_limit, j.toString());
//        System.out.println(br1);
//        ResultInfo<String> br2 = BlockUtils.blockTransaction("3e1d94b47867010e37c078f69326e1f1ce5417b2", "7cbe8fd74ade9f4cfdac85cce5960089116f3479", new BigInteger("10000", 10), BlockUtils.gas, BlockUtils.gas_limit, j.toString());
//        System.out.println(br2);
    }
}
