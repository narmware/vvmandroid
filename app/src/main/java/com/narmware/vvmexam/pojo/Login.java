package com.narmware.vvmexam.pojo;

import io.realm.RealmObject;

public class Login extends RealmObject {
    private String student_id,student_name,student_email,student_mobile,student_pincode,student_address,student_dob,student_aadhar;

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
}
