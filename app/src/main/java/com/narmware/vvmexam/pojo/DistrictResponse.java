package com.narmware.vvmexam.pojo;

public class DistrictResponse {
    String status_code;
    String status_message;
    District[] result;

    public String getStatus_code() {
        return status_code;
    }

    public String getStatus_message() {
        return status_message;
    }

    public District[] getResult() {
        return result;
    }
}
