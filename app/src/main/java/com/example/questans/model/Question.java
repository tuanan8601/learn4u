package com.example.questans.model;

import java.util.ArrayList;
import java.util.List;

public class Question {
    int qid;
    String title;
    String image;
    List<Answer> answers = new ArrayList<>();
    String solution;
    char solutionHead;
    String feedback;

    public int getQid() {
        return qid;
    }

    public void setQid(int qid) {
        this.qid = qid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public char getSolutionHead() {
        return solutionHead;
    }

    public void setSolutionHead(char solutionHead) {
        this.solutionHead = solutionHead;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Question(int qid, String title, String image, List<Answer> answers, String solution, char solutionHead, String feedback) {
        this.qid = qid;
        this.title = title;
        this.image = image;
        this.answers = answers;
        this.solution = solution;
        this.solutionHead = solutionHead;
        this.feedback = feedback;
    }

}