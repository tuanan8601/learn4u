package com.example.questans.model.result;

import com.example.questans.model.TestResult;

import java.util.LinkedList;
import java.util.List;

public class FullResult {
    TestResult testResult;
    List<Result> resultList;

    public FullResult() {
        resultList = new LinkedList<>();
    }

    public FullResult(TestResult testResult, List<Result> resultList) {
        this.testResult = testResult;
        this.resultList = resultList;
    }

    public TestResult getTestResult() {
        return testResult;
    }

    public void setTestResult(TestResult testResult) {
        this.testResult = testResult;
    }

    public List<Result> getResultList() {
        return resultList;
    }

    public void setResultList(List<Result> resultList) {
        this.resultList = resultList;
    }
}
