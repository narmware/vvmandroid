package com.narmware.vvmexam.pojo;

public class StateCoordResponse {
    String status_code,status_message,error_message;
    StateCoordDetails[] result;

    public String getStatus_code() {
        return status_code;
    }

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

    public StateCoordDetails[] getResult() {
        return result;
    }

    public void setResult(StateCoordDetails[] result) {
        this.result = result;
    }
}
