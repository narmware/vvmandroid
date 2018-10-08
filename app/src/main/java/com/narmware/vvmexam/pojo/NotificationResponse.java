package com.narmware.vvmexam.pojo;

public class NotificationResponse {
    String status_code,status_message,error_message;
    NotificationItems[] result;

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public String getStatus_message() {
        return status_message;
    }

    public void setStatus_message(String status_message) {
        this.status_message = status_message;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    public void setResult(NotificationItems[] result) {
        this.result = result;
    }

    public String getStatus_code() {
        return status_code;
    }

    public NotificationItems[] getResult() {
        return result;
    }
}
