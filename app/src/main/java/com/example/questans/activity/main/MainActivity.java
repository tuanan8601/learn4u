package com.example.questans.activity.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.Carousel;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.questans.activity.DocContributionActivity;
import com.example.questans.activity.SearchActivity;
import com.example.questans.activity.ProfileActivity;
import com.example.questans.R;
import com.example.questans.activity.login_signup.LoginActivity;
import com.example.questans.model.Subject;
import com.example.questans.activity.result.UserTestResultActivity;
import com.example.questans.activity.schedule.ScheduleActivity;
import com.example.questans.activity.subject.SubjectActivity;
import com.example.questans.user.AccountManager;
import com.example.questans.utils.ScheduleManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Menu mainMenu;
    Carousel carousel;
    Carousel.Adapter adapter;
    public final int LOGIN_CODE = 0;
    private SharedPreferences mPreferences;
    private String sharedPrefFile = "MainPref";
    BottomNavigationView bottomNavigationView;

    TextView txtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtSearch = findViewById(R.id.txtSearch);

        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.action_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        return true;

                    case R.id.action_history:
                        guideLoginMain();
                        startActivity(new Intent(getApplicationContext(), UserTestResultActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.action_profile:
                        guideLoginMain();
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        mPreferences = getSharedPreferences(sharedPrefFile,MODE_PRIVATE);
        String uidPref = mPreferences.getString ("uid_pref",null);
        String displayPref = mPreferences.getString("displayname_pref",null);
        AccountManager.setUid(uidPref);
        AccountManager.setDisplayName(displayPref);
        if(AccountManager.islogged()) ScheduleManager.dailyNoti(this);

        carousel = findViewById(R.id.carousel);
        setupCarousel(null);
//        ApiClient.getAPI().getAllSubjects().enqueue(new Callback<List<Subject>>() {
//            @Override
//            public void onResponse(Call<List<Subject>> call, Response<List<Subject>> response) {
//                if(response.body().size()>5)
//                    setupCarousel(response.body().subList(0,5));
//                else
//                    setupCarousel(response.body());
//            }
//
//            @Override
//            public void onFailure(Call<List<Subject>> call, Throwable t) {
//                setupCarousel(null);
//            }
//        });
    }

    public void setupCarousel(List<Subject> subjectList) {
        adapter = new Carousel.Adapter() {
            @Override
            public int count() {
                return 5;
            }

            @Override
            public void populate(View view, int index) {
//                if(subjectList.size()>index) {
//                    LinearLayout linearLayout = (LinearLayout) ((CardView) view).getChildAt(0);
//                    ImageView imageView = (ImageView) linearLayout.getChildAt(0);
//                    TextView textView = (TextView) linearLayout.getChildAt(1);
//                    if (subjectList.get(index).getPoster() != null && !subjectList.get(index).getPoster().trim().equals("")) {
//                        new DownloadImageTask(imageView).execute(subjectList.get(index).getPoster());
//                    }
//                    textView.setText(subjectList.get(index).getName());
//                }
            }

            @Override
            public void onNewItem(int index) {

            }
        };
        carousel.setAdapter(adapter);
    }

    public void toSubjectList(View view) {
        guideLoginMain();
        startActivity(new Intent(this, SubjectActivity.class));
    }

    public void toSearch(View view) {
        guideLoginMain();
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra("search_key",txtSearch.getText().toString());
        startActivity(intent);
    }

    public void toSchedule(View view) {
        guideLoginMain();
        startActivity(new Intent(this, ScheduleActivity.class));
    }
    public void toDocContri(View view) {
        guideLoginMain();
        startActivity(new Intent(this, DocContributionActivity.class));
    }
    public void guideLoginMain(){
        if(!AccountManager.islogged()){
            startActivityForResult(new Intent(this,LoginActivity.class),LOGIN_CODE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        mainMenu = menu;
        MenuItem item = mainMenu.findItem(R.id.action_login);
        if(AccountManager.islogged()){
            item.setIcon(getResources().getDrawable(R.drawable.account_logout_64));
        }
        else {
            item.setIcon(getResources().getDrawable(R.drawable.ic_baseline_login_24));
            bottomNavigationView.getMenu().findItem(R.id.action_profile).setTitle("Cá nhân");
        }
        setUserDisplayName();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_login) {
            if(AccountManager.islogged()){
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Đăng xuất")
                        .setMessage("Bạn có chắc muốn đăng xuất không?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                AccountManager.logout();
                                item.setIcon(getResources().getDrawable(R.drawable.ic_baseline_login_24));
                                bottomNavigationView.getMenu().findItem(R.id.action_profile).setTitle("Cá nhân");
                                ScheduleManager.cancelDailyNoti(MainActivity.this);

                                SharedPreferences.Editor preferencesEditor = mPreferences.edit();
                                preferencesEditor.putString("uid_pref", AccountManager.getUid());
                                preferencesEditor.putString("displayname_pref", AccountManager.getDisplayName());
                                preferencesEditor.apply();
                            }
                        })
                        .setNegativeButton(android.R.string.no,null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
            else {
                startActivityForResult(new Intent(this, LoginActivity.class),LOGIN_CODE);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LOGIN_CODE:
                MenuItem item = mainMenu.findItem(R.id.action_login);
                if(AccountManager.islogged()){
                    item.setIcon(getResources().getDrawable(R.drawable.account_logout_64));
                    ScheduleManager.dailyNoti(this);
                }
                else {
                    item.setIcon(getResources().getDrawable(R.drawable.ic_baseline_login_24));
                }
                setUserDisplayName();

                SharedPreferences.Editor preferencesEditor = mPreferences.edit();
                preferencesEditor.putString("uid_pref", AccountManager.getUid());
                preferencesEditor.putString("displayname_pref", AccountManager.getDisplayName());
                preferencesEditor.apply();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putString("uid_pref", AccountManager.getUid());
        preferencesEditor.putString("displayname_pref", AccountManager.getDisplayName());
        preferencesEditor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView.setSelectedItemId(R.id.action_home);
        if(mainMenu!=null) {
            MenuItem item = mainMenu.findItem(R.id.action_login);
            if (AccountManager.islogged()) {
                item.setIcon(getResources().getDrawable(R.drawable.account_logout_64));
            } else {
                item.setIcon(getResources().getDrawable(R.drawable.ic_baseline_login_24));
                bottomNavigationView.getMenu().findItem(R.id.action_profile).setTitle("Cá nhân");
            }
        }
        setUserDisplayName();
    }

    public void setUserDisplayName(){
        if(AccountManager.islogged())
            bottomNavigationView.getMenu().findItem(R.id.action_profile).setTitle(AccountManager.getDisplayName());
    }


}