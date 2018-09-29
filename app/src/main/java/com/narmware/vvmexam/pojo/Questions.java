package com.narmware.vvmexam.pojo;

import io.realm.RealmObject;

public class Questions extends RealmObject{

    private String qid,qname,answer;
    private int qAnswertype;

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