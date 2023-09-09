package com.example.questans.data;

import com.example.questans.model.Chapter;
import com.example.questans.model.ScheduleItem;
import com.example.questans.model.Subject;
import com.example.questans.model.TestResult;
import com.example.questans.model.User;
import com.example.questans.model.objTest.ObjectiveTest;
import com.example.questans.model.result.FullResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("objectivetest/{id}")
    Call<Chapter> getchaptersByID(@Path("id") String id);

    @POST("objectivetest/check")
    Call<TestResult> sendTestForm(@Body() TestResult testResult);

    @GET("testresult/full/{id}")
    Call<FullResult> getFullResultByID(@Path("id") String id);

    @GET("testresult/uid/{uid}")
    Call<List<TestResult>> getTestResultsbyUid(@Path("uid") String uid);

    @GET("objectivetest/chaptertest/{chapid}")
    Call<ObjectiveTest> getChapterTest(@Path("chapid") String chapid, @Query("time") int time, @Query("num") int num);

    @GET("objectivetest/subject/{subjId}")
    Call<List<Chapter>> getChapterbySubjectId(@Path("subjId") String subjId);

    @GET("objectivetest/subjecttest/{subjectId}")
    Call<ObjectiveTest> getSubjectTest(@Path("subjectId") String subjectId, @Query("time") int time, @Query("questperchap") int qpchap);

    //for schedule
    @GET("subject/{subjId}")
    Call<Subject> getSubjectbyId(@Path("subjId") String subjId);

    @GET("subject/all")
    Call<List<Subject>> getAllSubjects();

    @GET("user/schedule/{uid}")
    Call<List<ScheduleItem>> getAllSchedules(@Path("uid") String uid);

    @GET("user/schedule/{uid}/weekday/{wd}/shift/{sh}")
    Call<ScheduleItem> getSchedulebyDayandShift(@Path("uid") String uid,@Path("wd") int weekday,@Path("sh") int shift);

    @GET("user/schedule/{uid}/weekday/{wd}")
    Call<List<ScheduleItem>> getSchedulebyDay(@Path("uid") String uid,@Path("wd") int weekday);

    @PUT("user/schedule/{uid}")
    Call<String> updateUserByID(@Path("uid") String uid, @Body() ScheduleItem scheduleItem);

    @GET("user/{uid}")
    Call<User> getUserbyId(@Path("uid") String uid);

    @GET("user/username/{username}")
    Call<User> findUserbyUsername(@Path("username") String username);

    @POST("user/add")
    Call<String> addUser(@Body() User user);

    @PUT("user/{uid}")
    Call<String> editUser(@Path("uid") String uid, @Body User user);

    @GET("objectivetest/search")
    Call<List<Chapter>> searchChapter(@Query("text") String text);

    @GET("subject/search")
    Call<List<Subject>> searchSubject(@Query("text") String text);
}
