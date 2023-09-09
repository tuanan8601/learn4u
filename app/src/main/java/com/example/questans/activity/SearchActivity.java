package com.example.questans.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.questans.R;
import com.example.questans.activity.subject.ChapterListAdapter;
import com.example.questans.activity.subject.SubjectListAdapter;
import com.example.questans.data.ApiClient;
import com.example.questans.model.Chapter;
import com.example.questans.model.Subject;
import com.example.questans.user.AccountManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;

    EditText txtSearch;
    RadioButton optSubject, optChapter;
    TextView txtTitle;
    TextView txtNotFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if (!AccountManager.islogged()) finish();

        txtSearch = findViewById(R.id.txtSearch);
        optSubject = findViewById(R.id.optSubject);
        optChapter = findViewById(R.id.optChapter);
        txtTitle = findViewById(R.id.txtSearchTitle);
        txtNotFound = findViewById(R.id.txtNotFound);

        String key = getIntent().getStringExtra("search_key");
        if (key != null && !key.equals("")) {
            txtSearch.setText(key);
            txtTitle.setText("Kết quả tìm kiếm từ khóa\n\"" + key + "\"");
            doSearch();
        }
    }

    public void search(View view) {
        doSearch();
    }

    public void doSearch() {
        txtTitle.setText("Kết quả tìm kiếm từ khóa\n\"" + txtSearch.getText() + "\"");
        if (optSubject.isChecked()) {
            ApiClient.getAPI().searchSubject(txtSearch.getText().toString()).enqueue(new Callback<List<Subject>>() {
                @Override
                public void onResponse(Call<List<Subject>> call, Response<List<Subject>> response) {
                    if(response.body().size()<=0) txtNotFound.setText("Không tìm thấy kết quả nào!");
                    else txtNotFound.setText("");
                    setupRecycleView(response.body(), null);
                }

                @Override
                public void onFailure(Call<List<Subject>> call, Throwable t) {

                }
            });
        } else if (optChapter.isChecked()) {
            ApiClient.getAPI().searchChapter(txtSearch.getText().toString()).enqueue(new Callback<List<Chapter>>() {
                @Override
                public void onResponse(Call<List<Chapter>> call, Response<List<Chapter>> response) {
                    if(response.body().size()<=0) txtNotFound.setText("Không tìm thấy kết quả nào!");
                    else txtNotFound.setText("");
                    setupRecycleView(null, response.body());
                }

                @Override
                public void onFailure(Call<List<Chapter>> call, Throwable t) {

                }
            });
        }
    }

    public void setupRecycleView(List<Subject> subjectList, List<Chapter> chapterList) {
        mRecyclerView = findViewById(R.id.searchRecyclerView);
        if (optSubject.isChecked()) {
            mAdapter = new SubjectListAdapter(getApplicationContext(), subjectList);
        } else if (optChapter.isChecked()) {
            mAdapter = new ChapterListAdapter(getApplicationContext(), chapterList);
        }
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }
}