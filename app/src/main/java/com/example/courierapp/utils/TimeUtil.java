package com.example.courierapp.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public  class TimeUtil {

    public static String getCurrentTime()
    {
        long currenttime = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(currenttime);
        return dateFormat.format(date);
    }

    public static String getCurrentDate()
    {
        long currenttime = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(currenttime);
        return dateFormat.format(date);
    }
}
