package com.indicosmic.www.mypolicynow.model;

/**
 * Created by Ind3 on 16-01-18.
 */

public class InprogressInspectionModel {

    private String id,proposal_no,registration_no,ic_name,manf_date,vehicle_thumbnail;
    private String sub_inspec_date,registration_date;


    public InprogressInspectionModel() {
    }

    public InprogressInspectionModel(String id, String proposal_no, String registration_no, String ic_name, String manf_date, String vehicle_thumbnail) {
        this.id = id;
        this.proposal_no = proposal_no;
        this.registration_no = registration_no;
        this.ic_name = ic_name;
        this.manf_date = manf_date;
        this.vehicle_thumbnail = vehicle_thumbnail;
    }

    public String getRegistration_date() {
        return registration_date;
    }

    public void setRegistration_date(String registration_date) {
        this.registration_date = registration_date;
    }

    public String getSub_inspec_date() {
        return sub_inspec_date;
    }

    public void setSub_inspec_date(String sub_inspec_date) {
        this.sub_inspec_date = sub_inspec_date;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProposal_no() {
        return proposal_no;
    }

    public void setProposal_no(String proposal_no) {
        this.proposal_no = proposal_no;
    }

    public String getRegistration_no() {
        return registration_no;
    }

    public void setRegistration_no(String registration_no) {
        this.registration_no = registration_no;
    }

    public String getIc_name() {
        return ic_name;
    }

    public void setIc_name(String ic_name) {
        this.ic_name = ic_name;
    }

    public String getManf_date() {
        return manf_date;
    }

    public void setManf_date(String manf_date) {
        this.manf_date = manf_date;
    }

    public String getVehicle_thumbnail() {
        return vehicle_thumbnail;
    }

    public void setVehicle_thumbnail(String vehicle_thumbnail) {
        this.vehicle_thumbnail = vehicle_thumbnail;
    }
}
