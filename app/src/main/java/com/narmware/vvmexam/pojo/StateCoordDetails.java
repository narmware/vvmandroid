package com.narmware.vvmexam.pojo;

import io.realm.RealmObject;

public class StateCoordDetails extends RealmObject {
    private String co_ordinator_id,name,email,mobile,profile_img;

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }

    public String getCo_ordinator_id() {
        return co_ordinator_id;
    }

    public void setCo_ordinator_id(String co_ordinator_id) {
        this.co_ordinator_id = co_ordinator_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
