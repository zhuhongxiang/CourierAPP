package com.example.courierapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;

/*
 * NetworkUtil类为工具类用于判断当前网络环境是否可用
 */
public class NetworkUtil {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            if (cm == null) {
            } else {
                if (cm.getActiveNetworkInfo().isAvailable()) {
                    System.out.println("isAvailable");
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println(e+"");
        }

        return false;
    }
}
