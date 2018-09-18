package com.narmware.vvmexam.pojo;

public class NotificationResponse {
    String status_code;
    NotificationItems[] result;

    public String getStatus_code() {
        return status_code;
    }

    public NotificationItems[] getResult() {
        return result;
    }
}
