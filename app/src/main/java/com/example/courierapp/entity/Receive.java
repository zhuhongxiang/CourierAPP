package com.example.courierapp.entity;
/*
* 主要用于获取揽件所需的信息
*/
public class Receive {
    private String code;
    private String address;
    private String openCode;
    private String sendTime;
    public Receive() {
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getOpenCode() {
        return openCode;
    }
    public void setOpenCode(String openCode) {
        this.openCode = openCode;
    }
    public String getSendTime() {
        return sendTime;
    }
    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }
    @Override
    public String toString() {
        return "PickUp{" +
                "code='" + code + '\'' +
                ", address='" + address + '\'' +
                ", openCode='" + openCode + '\'' +
                ", sendTime='" + sendTime + '\'' +
                '}';
    }
}
