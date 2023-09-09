package com.example.questans.model.result;

import com.example.questans.model.Answer;
import com.example.questans.model.FormAnswer;
import com.example.questans.model.Question;

import java.util.ArrayList;
import java.util.Formattable;
import java.util.List;

public class Result {
    Question question;
    FormAnswer formAnswer;

    public Result() {
    }

    public Result(Question question, FormAnswer formAnswer) {
        this.question = question;
        this.formAnswer = formAnswer;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public FormAnswer getFormAnswer() {
        return formAnswer;
    }

    public void setFormAnswer(FormAnswer formAnswer) {
        this.formAnswer = formAnswer;
    }
}
