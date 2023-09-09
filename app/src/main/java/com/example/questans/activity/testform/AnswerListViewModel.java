package com.example.questans.activity.testform;

import androidx.lifecycle.ViewModel;

import com.example.questans.model.FormAnswer;

import java.util.List;

public class AnswerListViewModel extends ViewModel {
    List<FormAnswer> formAnswers;

    public List<FormAnswer> getFormAnswers() {
        return formAnswers;
    }

    public void setFormAnswers(List<FormAnswer> formAnswers) {
        this.formAnswers = formAnswers;
    }

    int curr;

    public int getCurr() {
        return curr;
    }

    public void setCurr(int curr) {
        this.curr = curr;
    }
}
