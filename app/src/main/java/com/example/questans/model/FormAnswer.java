package com.example.questans.model;

public class FormAnswer {
    String chapterId;
    int qid;
    String answerHead;
    boolean check;

    public FormAnswer(String chapterId, int qid) {
        this.chapterId = chapterId;
        this.qid = qid;
    }

    public FormAnswer(String chapterId, int qid, String answerHead) {
        this.chapterId = chapterId;
        this.qid = qid;
        this.answerHead = answerHead;
    }

    public FormAnswer(String chapterId, int qid, String answerHead, boolean check) {
        this.chapterId = chapterId;
        this.qid = qid;
        this.answerHead = answerHead;
        this.check = check;
    }

    public int getQid() {
        return qid;
    }

    public void setQid(int qid) {
        this.qid = qid;
    }

    public String getAnswerHead() {
        return answerHead;
    }

    public void setAnswerHead(String answerHead) {
        this.answerHead = answerHead;
    }

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

}
