package com.indicosmic.mypolicynow_app.model;

/**
 * Created by Ind3 on 15-12-17.
 */

public class Agentinfo {

    public String id;
    public String full_name;
    public String email;
    public String mobile_number;
    public String address;
    public String dob;
    public String imei;
    public String business_id;


    public Agentinfo() {
    }

    public Agentinfo(String id, String full_name, String email, String mobile_number, String address, String dob, String imei,String business_id) {
        this.id = id;
        this.full_name = full_name;
        this.email = email;
        this.mobile_number = mobile_number;
        this.address = address;
        this.dob = dob;
        this.imei = imei;
        this.business_id = business_id;
    }

    public String getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(String business_id) {
        this.business_id = business_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }
}
