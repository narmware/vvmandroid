package com.narmware.vvmexam.pojo;

import io.realm.RealmObject;

public class Login extends RealmObject {
    private String student_id,username,student_name,student_email,student_mobile,student_pincode,student_address,student_dob,student_aadhar
            ,student_state,student_dist,student_city,profile_path,state_id;
    private String sch_state,sch_dist,sch_city,sch_name;
    private String inst_id;

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public void setProfile_path(String profile_path) {
        this.profile_path = profile_path;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getInst_id() {
        return inst_id;
    }

    public void setInst_id(String inst_id) {
        this.inst_id = inst_id;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getStudent_email() {
        return student_email;
    }

    public void setStudent_email(String student_email) {
        this.student_email = student_email;
    }

    public String getStudent_mobile() {
        return student_mobile;
    }

    public void setStudent_mobile(String student_mobile) {
        this.student_mobile = student_mobile;
    }

    public String getStudent_pincode() {
        return student_pincode;
    }

    public void setStudent_pincode(String student_pincode) {
        this.student_pincode = student_pincode;
    }

    public String getStudent_address() {
        return student_address;
    }

    public void setStudent_address(String student_address) {
        this.student_address = student_address;
    }

    public String getStudent_dob() {
        return student_dob;
    }

    public void setStudent_dob(String student_dob) {
        this.student_dob = student_dob;
    }

    public String getStudent_aadhar() {
        return student_aadhar;
    }

    public void setStudent_aadhar(String student_aadhar) {
        this.student_aadhar = student_aadhar;
    }

    public String getStudent_state() {
        return student_state;
    }

    public void setStudent_state(String student_state) {
        this.student_state = student_state;
    }

    public String getStudent_dist() {
        return student_dist;
    }

    public void setStudent_dist(String student_dist) {
        this.student_dist = student_dist;
    }

    public String getStudent_city() {
        return student_city;
    }

    public void setStudent_city(String student_city) {
        this.student_city = student_city;
    }

    public String getSch_state() {
        return sch_state;
    }

    public void setSch_state(String sch_state) {
        this.sch_state = sch_state;
    }

    public String getSch_dist() {
        return sch_dist;
    }

    public void setSch_dist(String sch_dist) {
        this.sch_dist = sch_dist;
    }

    public String getSch_city() {
        return sch_city;
    }

    public void setSch_city(String sch_city) {
        this.sch_city = sch_city;
    }

    public String getSch_name() {
        return sch_name;
    }

    public void setSch_name(String sch_name) {
        this.sch_name = sch_name;
    }
}
