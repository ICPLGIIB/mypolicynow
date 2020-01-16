package com.indicosmic.www.mypolicynow.model;

/**
 * Created by Ind3 on 09-01-18.
 */

public class InprogressModel {

    //cutomer personal info
    private String customer_id,customer_fname,customer_lname,customer_phone,customer_address,customer_email,customer_created_at,shedule_date;

    //customer vehicle info
    private String inspection_id,vehicle_type_id,engine_no,chasis_no,make,model,variant,odometer_reading_id,yearof_manufacturing,register_no,fuel,image,vehicle_created_on;

    private String claim_no,pending_txt,referback_txt,submitted_date;
    private String workshop;
    private String admin_comment;


    public String getSubmitted_date() {
        return submitted_date;
    }

    public void setSubmitted_date(String submitted_date) {
        this.submitted_date = submitted_date;
    }

    public InprogressModel(String customer_id, String customer_fname, String customer_lname, String customer_phone, String customer_address, String customer_email, String customer_created_at, String shedule_date, String inspection_id, String vehicle_type_id, String engine_no, String chasis_no, String make, String model, String variant, String odometer_reading_id, String yearof_manufacturing, String register_no, String fuel, String image, String vehicle_created_on, String claim_no, String pending_txt, String referback_txt, String workshop) {
        this.customer_id = customer_id;
        this.customer_fname = customer_fname;
        this.customer_lname = customer_lname;
        this.customer_phone = customer_phone;
        this.customer_address = customer_address;
        this.customer_email = customer_email;
        this.customer_created_at = customer_created_at;
        this.shedule_date = shedule_date;
        this.inspection_id = inspection_id;
        this.vehicle_type_id = vehicle_type_id;
        this.engine_no = engine_no;
        this.chasis_no = chasis_no;
        this.make = make;
        this.model = model;
        this.variant = variant;
        this.odometer_reading_id = odometer_reading_id;
        this.yearof_manufacturing = yearof_manufacturing;
        this.register_no = register_no;
        this.fuel = fuel;
        this.image = image;
        this.vehicle_created_on = vehicle_created_on;
        this.claim_no = claim_no;
        this.pending_txt = pending_txt;
        this.referback_txt = referback_txt;
        this.workshop = workshop;
    }

    public String getAdmin_comment() {
        return admin_comment;
    }

    public void setAdmin_comment(String admin_comment) {
        this.admin_comment = admin_comment;
    }


    public InprogressModel() {
    }
    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_fname() {
        return customer_fname;
    }

    public void setCustomer_fname(String customer_fname) {
        this.customer_fname = customer_fname;
    }

    public String getCustomer_lname() {
        return customer_lname;
    }

    public void setCustomer_lname(String customer_lname) {
        this.customer_lname = customer_lname;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }

    public String getCustomer_address() {
        return customer_address;
    }

    public void setCustomer_address(String customer_address) {
        this.customer_address = customer_address;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public String getCustomer_created_at() {
        return customer_created_at;
    }

    public void setCustomer_created_at(String customer_created_at) {
        this.customer_created_at = customer_created_at;
    }

    public String getShedule_date() {
        return shedule_date;
    }

    public void setShedule_date(String shedule_date) {
        this.shedule_date = shedule_date;
    }

    public String getInspection_id() {
        return inspection_id;
    }

    public void setInspection_id(String inspection_id) {
        this.inspection_id = inspection_id;
    }

    public String getVehicle_type_id() {
        return vehicle_type_id;
    }

    public void setVehicle_type_id(String vehicle_type_id) {
        this.vehicle_type_id = vehicle_type_id;
    }

    public String getEngine_no() {
        return engine_no;
    }

    public void setEngine_no(String engine_no) {
        this.engine_no = engine_no;
    }

    public String getChasis_no() {
        return chasis_no;
    }

    public void setChasis_no(String chasis_no) {
        this.chasis_no = chasis_no;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public String getOdometer_reading_id() {
        return odometer_reading_id;
    }

    public void setOdometer_reading_id(String odometer_reading_id) {
        this.odometer_reading_id = odometer_reading_id;
    }

    public String getYearof_manufacturing() {
        return yearof_manufacturing;
    }

    public void setYearof_manufacturing(String yearof_manufacturing) {
        this.yearof_manufacturing = yearof_manufacturing;
    }

    public String getRegister_no() {
        return register_no;
    }

    public void setRegister_no(String register_no) {
        this.register_no = register_no;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVehicle_created_on() {
        return vehicle_created_on;
    }

    public void setVehicle_created_on(String vehicle_created_on) {
        this.vehicle_created_on = vehicle_created_on;
    }

    public String getClaim_no() {
        return claim_no;
    }

    public void setClaim_no(String claim_no) {
        this.claim_no = claim_no;
    }

    public String getPending_txt() {
        return pending_txt;
    }

    public void setPending_txt(String pending_txt) {
        this.pending_txt = pending_txt;
    }

    public String getReferback_txt() {
        return referback_txt;
    }

    public void setReferback_txt(String referback_txt) {
        this.referback_txt = referback_txt;
    }

    public String getWorkshop() {
        return workshop;
    }

    public void setWorkshop(String workshop) {
        this.workshop = workshop;
    }
}