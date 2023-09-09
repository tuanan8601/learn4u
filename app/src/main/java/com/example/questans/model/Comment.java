package com.example.questans.model;

import com.example.questans.utils.MyTime;

import java.util.Date;

public class Comment {
    String commentId;
    private String name;
    private String email;
    private String text;
    String objId;
    private Date date;
    private String photoURL;
    public String getTimeAgo() {
        return MyTime.timeAgo(new Date(), date);
    }
}