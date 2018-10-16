package com.narmware.vvmexam.pojo;

public class LanguageResponse {
    String status_code;
    String status_message;
    Language[] result;

    public String getStatus_code() {
        return status_code;
    }

    public String getStatus_message() {
        return status_message;
    }

    public Language[] getResult() {
        return result;
    }
}
