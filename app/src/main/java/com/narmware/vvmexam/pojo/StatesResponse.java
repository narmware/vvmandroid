package com.narmware.vvmexam.pojo;

public class StatesResponse {
    String status_code;
    String status_message;
    States[] result;

    public String getStatus_code() {
        return status_code;
    }

    public String getStatus_message() {
        return status_message;
    }

    public States[] getResult() {
        return result;
    }
}
