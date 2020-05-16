package com.example.courierapp.utils;


/*
 * 该类用于存储关于用户登录信息的全局变量
 * 如 userid，sid
 */

public class GlobalData {
    private static String uid;
    private static String name;
    private static String sid;
    private static String cabinetCode;
    private static int bigNumber;
    private static int middleNumber;
    private static int smallNumber;
    private static int tinyNumber;
    private static String bigCellType;
    private static String middleCellType;
    private static String smallCellType;
    private static String tinyCellType;
    private static String orderId;
    private static String pickupId;
    private static String receiveId;

    public static final String TAG = "TAG";

    public static final String URL_HEAD = "http://39.97.223.16";

    public static String getUid() {
        return uid;
    }

    public static void setUid(String uid) {
        GlobalData.uid = uid;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        GlobalData.name = name;
    }

    public static String getSid() {
        return sid;
    }

    public static void setSid(String sid) {
        GlobalData.sid = sid;
    }

    public static String getCabinetCode() {
        return cabinetCode;
    }

    public static void setCabinetCode(String cabinetCode) {
        GlobalData.cabinetCode = cabinetCode;
    }

    public static int getBigNumber() {
        return bigNumber;
    }

    public static void setBigNumber(int bigNumber) {
        GlobalData.bigNumber = bigNumber;
    }

    public static int getMiddleNumber() {
        return middleNumber;
    }

    public static void setMiddleNumber(int middleNumber) {
        GlobalData.middleNumber = middleNumber;
    }

    public static int getSmallNumber() {
        return smallNumber;
    }

    public static void setSmallNumber(int smallNumber) {
        GlobalData.smallNumber = smallNumber;
    }

    public static int getTinyNumber() {
        return tinyNumber;
    }

    public static void setTinyNumber(int tinyNumber) {
        GlobalData.tinyNumber = tinyNumber;
    }

    public static String getBigCellType() {
        return bigCellType;
    }

    public static void setBigCellType(String bigCellType) {
        GlobalData.bigCellType = bigCellType;
    }

    public static String getMiddleCellType() {
        return middleCellType;
    }

    public static void setMiddleCellType(String middleCellType) {
        GlobalData.middleCellType = middleCellType;
    }

    public static String getSmallCellType() {
        return smallCellType;
    }

    public static void setSmallCellType(String smallCellType) {
        GlobalData.smallCellType = smallCellType;
    }

    public static String getTinyCellType() {
        return tinyCellType;
    }

    public static void setTinyCellType(String tinyCellType) {
        GlobalData.tinyCellType = tinyCellType;
    }

    public static String getOrderId() {
        return orderId;
    }

    public static void setOrderId(String orderId) {
        GlobalData.orderId = orderId;
    }

    public static String getPickupId() {
        return pickupId;
    }

    public static void setPickupId(String pickupId) {
        GlobalData.pickupId = pickupId;
    }

    public static String getReceiveId() {
        return receiveId;
    }

    public static void setReceiveId(String receiveId) {
        GlobalData.receiveId = receiveId;
    }
}
