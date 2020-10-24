package com.bld.project.task;

import com.bld.project.system.block.BlockController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.crypto.CipherException;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * @author SOFAS
 * @date 2020/7/18
 * @directions  thingsboard数据同步
*/
@Slf4j
@Component
public class DataSynchronousTask {
    @Resource
    private BlockController blockController;

    @Scheduled(cron = "0 0 0 * * ？")
    public void doing() throws NoSuchProviderException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, CipherException, IOException {
        long beginTime = System.currentTimeMillis();
        log.info("**********************************开始同步数据，开始时间：{}**********************************", beginTime);
        blockController.dataSynchronous();
        long endTime = System.currentTimeMillis();
        log.info("**********************************同步数据完成，结束时间：{}**********************************", endTime);
    }
}
