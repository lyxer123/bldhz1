package com.bld.project.newlyadded.untils;

import com.bld.project.newlyadded.mapper.BesuFileMapper;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class insertFileSize implements ApplicationRunner {



    @Autowired
    private ScheduledExecutorService scheduledExecutorService;

    @Autowired
    private BesuFileMapper besuFileMapper;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        long timeMillis = getTimeMillis("11:03:00");
        System.out.println("进入");
        long oneDay = 24 * 60 * 60 * 1000;
        long initDelay = timeMillis- System.currentTimeMillis();
        initDelay = initDelay>0?initDelay:initDelay+oneDay;
        scheduledExecutorService.scheduleAtFixedRate(()->{
            System.out.println("kaishi");
            besuFileMapper.insertFile(FileUtils.sizeOf(new File("/usr/local/besu"))/1024/1024);
        },initDelay,oneDay, TimeUnit.MILLISECONDS);

    }


    private  long getTimeMillis(String time) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
            DateFormat dayFormat = new SimpleDateFormat("yy-MM-dd");
            Date curDate = dateFormat.parse(dayFormat.format(new Date()) + " " + time);
            return curDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
