package com.greendao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sheng on 2016/4/24.
 */
public class Test {
    public static void main(String[] args) throws ParseException {
        /*String date = "2016-04-22T19:13:58+08:00";
        String[] tempStr = date.split("T");
        String day = tempStr[0];
        String time = tempStr[1].split("\\+")[0];

        String result = day +" "+time;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date tempDate = format.parse(result);

        System.out.println(tempDate.toString());*/
        String date = "2016-04-22T19:13:58+08:00";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date tempDate = format.parse(date);
        System.out.print(tempDate);
    }
}
