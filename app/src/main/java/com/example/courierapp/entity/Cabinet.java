package com.example.courierapp.entity;

public class Cabinet {

    private String cab_id;
    private String cab_name;
    private String addr;

    public Cabinet(){

    }

    public Cabinet(String cab_id, String cab_name, String addr) {
        this.cab_id = cab_id;
        this.cab_name = cab_name;
        this.addr = addr;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getCab_id() {
        return cab_id;
    }

    public void setCab_id(String cab_id) {
        this.cab_id = cab_id;
    }

    public String getCab_name() {
        return cab_name;
    }

    public void setCab_name(String cab_name) {
        this.cab_name = cab_name;
    }

    @Override
    public String toString() {
        return "Cabinet{" +
                "cab_id='" + cab_id + '\'' +
                ", cab_name='" + cab_name + '\'' +
                ", addr='" + addr + '\'' +
                '}';
    }
}
