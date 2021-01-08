package com.bld.project.utils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Test {
    public static void main(String[] args) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(1607413947427L);
        System.out.println(format);

        String format1 = simpleDateFormat.format(1607398551723L);
        System.out.println(format1);
        BigDecimal bigDecimal = new BigDecimal("21");
        System.out.println(bigDecimal.multiply(new BigDecimal((double) 1 / 60)).setScale(4, BigDecimal.ROUND_HALF_UP).stripTrailingZeros());
    }
}
