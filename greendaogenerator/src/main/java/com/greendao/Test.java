package com.greendao;

import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * Created by sheng on 2016/4/24.
 */
public class Test {
    public static void main(String[] args) throws ParseException {

        String url = "http://pic.mmfile.net/2016/04/21a01.jpg";
        int index = 1;
        String imgUrl = url.substring(0, 33) + new DecimalFormat("00").format(index) + ".jpg";
        System.out.println(imgUrl);
    }
}
