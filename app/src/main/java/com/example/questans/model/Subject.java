package com.example.questans.model;

import java.util.*;

public class Subject {
    String subjectId;
    String name;
    String type;
    String poster;
    String document;
    List<String> objIds = new ArrayList<>();
//    Map<String, ObjectId> objectiveTest_map;
//    LinkedHashMap<String, String> objectiveTest_name_id = new LinkedHashMap<>();

    public Subject() {
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public List<String> getObjIds() {
        return objIds;
    }

    public void setObjIds(List<String> objIds) {
        this.objIds = objIds;
    }
}
