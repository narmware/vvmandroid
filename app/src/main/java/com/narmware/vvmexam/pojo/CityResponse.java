package com.narmware.vvmexam.pojo;

public class CityResponse {
    String status_code;
    String status_message;
    City[] result;

    public String getStatus_code() {
        return status_code;
    }

    public String getStatus_message() {
        return status_message;
    }

    public City[] getResult() {
        return result;
    }
}
