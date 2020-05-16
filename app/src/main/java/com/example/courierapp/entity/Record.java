package com.example.courierapp.entity;
/*
 * 主要用于获取投递记录信息
 */
public class Record {
    private String deliveryNumber;
    private String deliveryTime;
    private String status;
    public Record() {

    }
    public Record(String deliveryNumber, String deliveryTime, String status) {
        this.deliveryNumber = deliveryNumber;
        this.deliveryTime = deliveryTime;
        this.status = status;
    }
    public String getDeliveryNumber() {
        return deliveryNumber;
    }
    public void setDeliveryNumber(String deliveryNumber) {
        this.deliveryNumber = deliveryNumber;
    }
    public String getDeliveryTime() {
        return deliveryTime;
    }
    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    @Override
    public String toString() {
        return "Record{" +
                "deliveryNumber='" + deliveryNumber + '\'' +
                ", deliveryTime='" + deliveryTime + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
