package com.narmware.vvmexam.pojo;

public class LoginResponse {

    String status_code;
    String status_message,error_message;
    Login result;

    public String getStatus_code() {
        return status_code;
    }

    public String getStatus_message() {
        return status_message;
    }

    public String getError_message() {
        return error_message;
    }

    public Login getResult() {
        return result;
    }
}
