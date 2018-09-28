package com.narmware.vvmexam.pojo;

import io.realm.RealmObject;

public class QuestionSequenceType extends RealmObject {
  private String qnum,qtype;

    public String getQnum() {
        return qnum;
    }

    public void setQnum(String qnum) {
        this.qnum = qnum;
    }

    public String getQtype() {
        return qtype;
    }

    public void setQtype(String qtype) {
        this.qtype = qtype;
    }
}
