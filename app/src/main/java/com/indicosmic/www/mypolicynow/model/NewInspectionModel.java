package com.indicosmic.www.mypolicynow.model;

/**
 * Created by Ind3 on 15-01-18.
 */

public class NewInspectionModel {
    private String id,proposal_no,registration_no,ic_name,manf_date,vehicle_thumbnail;
    private String policy_start_date,policy_end_date;


    public NewInspectionModel() {
    }

    public NewInspectionModel(String id, String proposal_no, String registration_no, String ic_name, String manf_date, String vehicle_thumbnail) {
        this.id = id;
        this.proposal_no = proposal_no;
        this.registration_no = registration_no;
        this.ic_name = ic_name;
        this.manf_date = manf_date;
        this.vehicle_thumbnail = vehicle_thumbnail;
    }

    public String getPolicy_start_date() {
        return policy_start_date;
    }

    public void setPolicy_start_date(String policy_start_date) {
        this.policy_start_date = policy_start_date;
    }

    public String getPolicy_end_date() {
        return policy_end_date;
    }

    public void setPolicy_end_date(String policy_end_date) {
        this.policy_end_date = policy_end_date;
    }

    public String getVehicle_thumbnail() {
        return vehicle_thumbnail;
    }

    public void setVehicle_thumbnail(String vehicle_thumbnail) {
        this.vehicle_thumbnail = vehicle_thumbnail;
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
}
