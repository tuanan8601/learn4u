package com.example.questans.activity.subject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.questans.R;
import com.example.questans.data.ApiClient;
import com.example.questans.model.Subject;
import com.example.questans.user.AccountManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubjectActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    SubjectListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        if(!AccountManager.islogged()) finish();

        ApiClient.getAPI().getAllSubjects().enqueue(new Callback<List<Subject>>() {
            @Override
            public void onResponse(Call<List<Subject>> call, Response<List<Subject>> response) {
                setupRecycleView(response.body());
            }

            @Override
            public void onFailure(Call<List<Subject>> call, Throwable t) {

            }
        });
    }

    public void setupRecycleView(List<Subject> subjectList){
        mRecyclerView = findViewById(R.id.subject_recycleview);
        mAdapter = new SubjectListAdapter(getApplicationContext(),subjectList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }
}