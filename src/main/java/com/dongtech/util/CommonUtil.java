package com.dongtech.util;


import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtil {
    private static long index = 1;
    public static String getNumber(String str){
        String newNumber="";
        //获取当前的8位日期
        Date date=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
        String time=sdf.format(date);
        newNumber=str+time+System.currentTimeMillis();
        return newNumber;
    }

}
