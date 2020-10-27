package com.bld.newlyadded.untils;


import com.bld.newlyadded.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class SaticScheduleTask implements ApplicationRunner {


    @Autowired
    private WebSocketServer webSocketServer;


    @Autowired
    private ScheduledExecutorService scheduledExecutorService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        scheduledExecutorService.scheduleAtFixedRate(()->{
            //System.out.println("线程启动");
            webSocketServer.sendMessage();
        }, 0, 3, TimeUnit.SECONDS);
    }
}
