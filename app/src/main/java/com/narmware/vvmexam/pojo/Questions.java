package com.narmware.vvmexam.pojo;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Questions extends RealmObject{
    private String qid;

    private String qname,answer,date_time;
    private int qAnswertype;

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
