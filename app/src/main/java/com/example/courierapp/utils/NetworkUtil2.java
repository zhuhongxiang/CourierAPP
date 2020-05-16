package com.example.courierapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络工具类
 *
 * @author rendayun
 */
public final class NetworkUtil2 {

    /**
     * 构造函数
     */
    private NetworkUtil2() {

    }

    /**
     * 网络是否可用。
     *
     * @param context context
     *
     * @return 连接并可用返回 true
     */
    public static boolean isNetworkConnected(Context context) {
        NetworkInfo networkInfo = getActiveNetworkInfo(context);
        boolean flag = networkInfo != null && networkInfo.isAvailable();
        return flag;
    }

    /**
     * wifi网络是否可用
     *
     * @param context context
     *
     * @return wifi连接并可用返回 true
     */
    public static boolean isWifiNetworkConnected(Context context) {
        NetworkInfo networkInfo = getActiveNetworkInfo(context);
        boolean flag =
                networkInfo != null && networkInfo.isAvailable()
                        && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
        return flag;

    }

    /**
     * 获取活动的连接。
     *
     * @param context context
     *
     * @return 当前连接
     */
    public static NetworkInfo getActiveNetworkInfo(Context context) {
        if (context == null) {
            return null;
        }
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return null;
        }
        return connectivity.getActiveNetworkInfo();
    }

    /**
     * 手机网络是否可用
     *
     * @param context context
     *
     * @return 手机网络连接并可用返回 true
     */
    public static boolean isMobileNetworkConnected(Context context) {
        NetworkInfo networkInfo = getActiveNetworkInfo(context);
        boolean flag =
                networkInfo != null && networkInfo.isAvailable()
                        && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
        return flag;
    }



}