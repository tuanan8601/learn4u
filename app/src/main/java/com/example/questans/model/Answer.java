package com.example.questans.model;

public class Answer {
    String answer;
    char answerHead;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public char getAnswerHead() {
        return answerHead;
    }

    public void setAnswerHead(char answerHead) {
        this.answerHead = answerHead;
    }

    public Answer(String answer, char answerHead) {
        this.answer = answer;
        this.answerHead = answerHead;
    }
}
