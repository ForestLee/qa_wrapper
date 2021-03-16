package com.forest.insurance;

import java.util.ArrayList;
import java.util.List;

public class InsuranceResponse {
    private int code;
    private List<Insurance> data;
    private String message;

    public List<Insurance> getData() {
        return data;
    }

    public void setData(List<Insurance> data) {
        this.data = data;
    }

    public InsuranceResponse() {
        code = 0;
        message = "";
        data = new ArrayList<Insurance>(1);
    }

    public void addData(Insurance insurance) {
        if (!data.isEmpty())
            data.clear();
        data.add(insurance);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "{" +
                "code=" + code +
                ", data=" + data.get(0).toString() +
                ", message='" + message + '\'' +
                '}';
    }

}
