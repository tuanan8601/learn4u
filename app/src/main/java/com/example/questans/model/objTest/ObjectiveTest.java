package com.example.questans.model.objTest;

import java.util.*;

public class ObjectiveTest {
    String testName;
    String subjId;
    int time;
    List<TestQuest> testQuestList = new LinkedList<>();

    public ObjectiveTest() {
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

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public List<TestQuest> getTestQuestList() {
        return testQuestList;
    }

    public void setTestQuestList(List<TestQuest> testQuestList) {
        this.testQuestList = testQuestList;
    }
}
