package com.example.questans.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TestResult {
    String userId;
    String formId;
    String testName;
    String csId;
    int type;
    int score;
    int totalScore;
    int time;
    int dotime;
    long createdAt;

    List<FormAnswer> formAnswers;

    public TestResult() {
    }

    public TestResult(String formId, String testName, int score, int totalScore, int time, int dotime, List<FormAnswer> formAnswers) {
        this.formId = formId;
        this.testName = testName;
        this.score = score;
        this.totalScore = totalScore;
        this.time = time;
        this.dotime = dotime;
        this.formAnswers = formAnswers;
    }

    public String getCsId() {
        return csId;
    }

    public void setCsId(String csId) {
        this.csId = csId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getCreatedAt() {
        return new Date(createdAt);
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getDotime() {
        return dotime;
    }

    public void setDotime(int dotime) {
        this.dotime = dotime;
    }

    public List<FormAnswer> getFormAnswers() {
        return formAnswers;
    }

    public void setFormAnswers(List<FormAnswer> formAnswers) {
        this.formAnswers = formAnswers;
    }
}
