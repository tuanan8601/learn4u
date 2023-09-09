package com.example.questans.user;

import com.example.questans.data.ApiClient;
import com.example.questans.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountManager {
    private static String uid;
    private static String displayName;

    public static String getUid() {
        return uid;
    }

    public static void setUid(String uid) {
        AccountManager.uid = uid;
    }

    public static String getDisplayName() {
        return displayName;
    }

    public static void setDisplayName(String displayName) {
        AccountManager.displayName = displayName;
    }

    public static boolean islogged(){
        return uid!=null;
    }
    public static void getUser(Callback<User> callback){
        ApiClient.getAPI().getUserbyId(uid).enqueue(callback);
    }
    public static void login(String username, String password, Callback<User> call){
        System.out.println(username+" "+password);
        ApiClient.getAPI().findUserbyUsername(username).enqueue(call);
    }
    public static void logout(){
        uid = null;
        displayName = null;
    }
}
