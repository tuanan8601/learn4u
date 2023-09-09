package com.example.questans.activity.subject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.questans.R;
import com.example.questans.data.ApiClient;
import com.example.questans.model.Chapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChapterActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    ChapterListAdapter mAdapter;

    TextView txtChuong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

        txtChuong = findViewById(R.id.txtChuong);

        String subjectId = getIntent().getStringExtra("subjId");
        String subjectName = getIntent().getStringExtra("subjName");

        txtChuong.setText("Các chương của môn\n"+subjectName);
        ApiClient.getAPI().getChapterbySubjectId(subjectId).enqueue(new Callback<List<Chapter>>() {
            @Override
            public void onResponse(Call<List<Chapter>> call, Response<List<Chapter>> response) {
                setupRecycleView(response.body());
            }

            @Override
            public void onFailure(Call<List<Chapter>> call, Throwable t) {

            }
        });
    }

    public void setupRecycleView(List<Chapter> chapterList){
        mRecyclerView = findViewById(R.id.chapter_recycleview);
        mAdapter = new ChapterListAdapter(getApplicationContext(),chapterList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }
}