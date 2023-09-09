package com.example.questans.activity.result;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.questans.R;
import com.example.questans.data.ApiClient;
import com.example.questans.model.Answer;
import com.example.questans.model.FormAnswer;
import com.example.questans.model.Question;
import com.example.questans.model.result.FullResult;
import com.example.questans.model.result.Result;
import com.example.questans.activity.testform.TestFormActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestResultActivity extends AppCompatActivity {
    TextView txtResultTitle;
    TextView txtScore;
    TextView txtTime;
    RecyclerView mRecyclerView;
    ResultListAdapter mAdapter;
    FullResult fullResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_result);
        txtResultTitle = findViewById(R.id.txtResultTitle);
        txtScore = findViewById(R.id.txtScore);
        txtTime = findViewById(R.id.txtTime);

        String resultId = getIntent().getStringExtra("resultID");
        if(resultId!=null) {
            ApiClient.getAPI().getFullResultByID(resultId).enqueue(new Callback<FullResult>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<FullResult> call, Response<FullResult> response) {
                    fullResult = response.body();
                    Log.d("An_test", "onResponse: " + fullResult);
                    setupRecycleView();
                    txtResultTitle.setText(fullResult.getTestResult().getTestName());
                    txtScore.setText("Điểm: "+fullResult.getTestResult().getScore()+"/"+fullResult.getTestResult().getTotalScore());
                    if(fullResult.getTestResult().getTime()>0)
                        txtTime.setText("Thời gian: "+fullResult.getTestResult().getDotime()+" phút / "+fullResult.getTestResult().getTime()+" phút");
                }

                @Override
                public void onFailure(Call<FullResult> call, Throwable t) {
                    Log.d("An_test", "Error:" + t.getMessage());
                    setFullResult();
                    setupRecycleView();
                }
            });
        }
    }

    public void setupRecycleView(){
        mRecyclerView = findViewById(R.id.resultRecycleView);
        mAdapter = new ResultListAdapter(getApplicationContext(),fullResult.getResultList());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    public void setFullResult(){
        fullResult = new FullResult();
        List<Answer> answers = new ArrayList<>();
        answers.add(new Answer("A.123",'A'));
        answers.add(new Answer("B.234",'B'));
        answers.add(new Answer("C.345",'C'));
        answers.add(new Answer("D.456",'D'));
        for (int i = 0; i < 32; i++) {
            Result result = new Result(new Question(i+1,"Question "+(i+1),null,answers,"C.345",'C',null), new FormAnswer("abc",i+1,"A",false));
            fullResult.getResultList().add(result);
        }
    }

    public void doAgain(View view) {
        int type = fullResult.getTestResult().getType();
        Intent intent = new Intent(getApplicationContext(), TestFormActivity.class);
        intent.putExtra("type",type);
        if(type==0||type==1){
            intent.putExtra("chapId",fullResult.getTestResult().getCsId());
        }
        else if(type==2){
            intent.putExtra("subjId",fullResult.getTestResult().getCsId());
        }
        startActivity(intent);
    }
}