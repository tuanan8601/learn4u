package com.example.questans.activity.result;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.questans.R;
import com.example.questans.activity.ProfileActivity;
import com.example.questans.activity.login_signup.LoginActivity;
import com.example.questans.activity.main.MainActivity;
import com.example.questans.data.ApiClient;
import com.example.questans.model.TestResult;
import com.example.questans.model.User;
import com.example.questans.user.AccountManager;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserTestResultActivity extends AppCompatActivity {
    Menu mainMenu;
    RecyclerView mRecyclerView;
    UserTestResultListAdapter mAdapter;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_test_result);

        if(!AccountManager.islogged()) finish();

        bottomNavigationView = findViewById(R.id.bottomNav);
        setUserDisplayName();
        bottomNavigationView.setSelectedItemId(R.id.action_history);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        finish();
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.action_history:
                        return true;

                    case R.id.action_profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                }
                return false;
            }
        });

        ApiClient.getAPI().getTestResultsbyUid(AccountManager.getUid()).enqueue(new Callback<List<TestResult>>() {
            @Override
            public void onResponse(Call<List<TestResult>> call, Response<List<TestResult>> response) {
                if(response.body()!=null)
                    setupRecycleView(response.body());
            }

            @Override
            public void onFailure(Call<List<TestResult>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void setupRecycleView(List<TestResult> testResultList){
        System.out.println("ttt");
        mRecyclerView = findViewById(R.id.user_testresult_recycleview);
        mAdapter = new UserTestResultListAdapter(getApplicationContext(),testResultList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        mainMenu=menu;
        MenuItem item = mainMenu.findItem(R.id.action_login);
        if(AccountManager.islogged()){
            item.setIcon(getResources().getDrawable(R.drawable.account_logout_64));
        }
        else {
            item.setIcon(getResources().getDrawable(R.drawable.ic_baseline_login_24));
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_login) {
            if(AccountManager.islogged()){
                new AlertDialog.Builder(UserTestResultActivity.this)
                        .setTitle("Đăng xuất")
                        .setMessage("Bạn có chắc muốn đăng xuất không?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                AccountManager.logout();
                                item.setIcon(getResources().getDrawable(R.drawable.ic_baseline_login_24));
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no,null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void setUserDisplayName(){
        if(AccountManager.islogged())
            bottomNavigationView.getMenu().findItem(R.id.action_profile).setTitle(AccountManager.getDisplayName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mainMenu!=null) {
            MenuItem item = mainMenu.findItem(R.id.action_login);
            if (AccountManager.islogged()) {
                item.setIcon(getResources().getDrawable(R.drawable.account_logout_64));
            } else {
                item.setIcon(getResources().getDrawable(R.drawable.ic_baseline_login_24));
            }
        }
    }
}