package com.example.questans.model;

import java.util.ArrayList;
import java.util.List;

public class Chapter {
    String chapterId;
    String testName;
    String subjId;
    String poster;
    List<Question> questions = new ArrayList<>();

    public Chapter(String chapterId, String testName, String subjId, String poster, List<Question> questions) {
        this.chapterId = chapterId;
        this.testName = testName;
        this.subjId = subjId;
        this.poster = poster;
        this.questions = questions;
    }

    public String getchapterId() {
        return chapterId;
    }

    public void setchapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getSubjId() {
        return subjId;
    }

    public void setSubjId(String subjId) {
        this.subjId = subjId;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
