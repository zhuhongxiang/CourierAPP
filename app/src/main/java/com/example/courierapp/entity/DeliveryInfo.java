package com.example.courierapp.entity;

public class DeliveryInfo {

    private String sendinfo_id;
    private String id;
    private String courier_id;
    private String rec_phone;
    private String rec_addr;
    private String rec_detail;


    public DeliveryInfo(){

    }

    public DeliveryInfo(String sendinfo_id, String id, String courier_id) {
        this.sendinfo_id = sendinfo_id;
        this.id = id;
        this.courier_id = courier_id;

    }

    public void setSendinfo_id(String sendinfo_id) {
        this.sendinfo_id = sendinfo_id;
    }

    public String getSendinfo_id() {
        return sendinfo_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourier_id() {
        return courier_id;
    }

    public void setCourier_id(String courier_id) {
        this.courier_id = courier_id;
    }

    public String getRec_phone() {
        return rec_phone;
    }

    public void setRec_phone(String rec_phone) {
        this.rec_phone = rec_phone;
    }

    public String getRec_addr() {
        return rec_addr;
    }

    public void setRec_addr(String rec_addr) {
        this.rec_addr = rec_addr;
    }

    public String getRec_detail() {
        return rec_detail;
    }

    public void setRec_detail(String rec_detail) {
        this.rec_detail = rec_detail;
    }

    @Override
    public String toString() {
        return "DeliveryInfo{" +
                "sendinfo_id='" + sendinfo_id + '\'' +
                ", id='" + id + '\'' +
                ", courier_id='" + courier_id + '\'' +
                "rec_phone='" + rec_phone + '\'' +
                ", rec_addr='" + rec_addr + '\'' +
                ", rec_detail='" + rec_detail + '\'' +
                '}';
    }
}
