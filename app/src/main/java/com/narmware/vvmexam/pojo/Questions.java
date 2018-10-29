package com.narmware.vvmexam.pojo;

import android.graphics.Bitmap;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Questions extends RealmObject{
    private String qid;

    private String qname,answer,date_time;
    private int qAnswertype;
    private String bitmap;
    private String Engj,Engs;
    private String Hinj,Hins;
    private String Marj,Mars;
    private String Telj,Tels;
    private String Tamj,Tams;

    public String getEngj() {
        return Engj;
    }

    public void setEngj(String engj) {
        Engj = engj;
    }

    public String getEngs() {
        return Engs;
    }

    public void setEngs(String engs) {
        Engs = engs;
    }

    public String getHinj() {
        return Hinj;
    }

    public void setHinj(String hinj) {
        Hinj = hinj;
    }

    public String getHins() {
        return Hins;
    }

    public void setHins(String hins) {
        Hins = hins;
    }

    public String getMarj() {
        return Marj;
    }

    public void setMarj(String marj) {
        Marj = marj;
    }

    public String getMars() {
        return Mars;
    }

    public void setMars(String mars) {
        Mars = mars;
    }

    public String getTelj() {
        return Telj;
    }

    public void setTelj(String telj) {
        Telj = telj;
    }

    public String getTels() {
        return Tels;
    }

    public void setTels(String tels) {
        Tels = tels;
    }

    public String getTamj() {
        return Tamj;
    }

    public void setTamj(String tamj) {
        Tamj = tamj;
    }

    public String getTams() {
        return Tams;
    }

    public void setTams(String tams) {
        Tams = tams;
    }

    public String getBitmap() {
        return bitmap;
    }

    public void setBitmap(String bitmap) {
        this.bitmap = bitmap;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public int getqAnswertype() {
        return qAnswertype;
    }

    public void setqAnswertype(int qAnswertype) {
        this.qAnswertype = qAnswertype;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getQname() {
        return qname;
    }

    public void setQname(String qname) {
        this.qname = qname;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
