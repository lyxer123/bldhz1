package com.bld.project.utils;

import java.util.regex.Pattern;

/**
 * @author SOFAS
 * @date 2020/7/7
 * @directions  正则表达式
*/
public class RegularUtils {
    public static String COORDINATE = "(?:[0-9]|[1-9][0-9]|1[0-7][0-9]|180)\\.([0-9]{6})+,(?:[0-9]|[1-8][0-9]|90)\\.([0-9]{6})";

    public static boolean verifyReg(String reg, String s){
        return Pattern.matches(reg, s);
    }
}
