package com.example.questans.activity.schedule;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.questans.data.ApiClient;
import com.example.questans.R;
import com.example.questans.model.ScheduleItem;
import com.example.questans.user.AccountManager;
import com.example.questans.utils.ScheduleManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleActivity extends AppCompatActivity implements ScheduleDialogFragment.OnInputListener{
    ConstraintLayout layout;
    List<String> weekday = new ArrayList<>();
    int saveThu;
    int saveCa;
    private SharedPreferences mPreferences;
    private String sharedPrefFile = "MainPref";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        mPreferences = getSharedPreferences(sharedPrefFile,MODE_PRIVATE);
        String uidPref = mPreferences.getString ("uid_pref",null);
        AccountManager.setUid(uidPref);
        if(!AccountManager.islogged()) finish();

        layout = findViewById(R.id.scheduleLayout);

        weekday.add(null);
        weekday.add("CN");
        weekday.add("T2");
        weekday.add("T3");
        weekday.add("T4");
        weekday.add("T5");
        weekday.add("T6");
        weekday.add("T7");
        loadData();
    }

    public void loadData(){
        System.out.println("is now loading!");
        ApiClient.getAPI().getAllSchedules(AccountManager.getUid()).enqueue(new Callback<List<ScheduleItem>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<ScheduleItem>> call, Response<List<ScheduleItem>> response) {
                List<ScheduleItem> scheduleItemList = response.body();
                System.out.println("Load!");
                for (int i = 1; i <=7 ; i++) {
                    for (int j = 1; j <=10 ; j++) {
                        setTableText(weekday.get(i),j,"");
                    }
                }
                if(scheduleItemList!=null) {
                    scheduleItemList.forEach(d -> {
                        setTableText(weekday.get(d.getWeekday()), d.getShift(), d.getSubject());
                    });
                }
            }

            @Override
            public void onFailure(Call<List<ScheduleItem>> call, Throwable t) {
                System.out.println(t);
            }
        });
    }

//    public void setSubjectNamebyId(String thu,int ca,String subjId){
//        ApiClient.getAPI().getSubjectbyId(subjId).enqueue(new Callback<Subject>() {
//            @Override
//            public void onResponse(Call<Subject> call, Response<Subject> response) {
//                Subject subject = response.body();
//                setTableText(thu,ca,subject.getName());
//            }
//
//            @Override
//            public void onFailure(Call<Subject> call, Throwable t) {
//
//            }
//        });
//    }

    public void setTableText(String thu,int ca, String text){
        int viewId = getResources().getIdentifier("txt"+thu+"C"+ca, "id", ScheduleActivity.this.getPackageName());
        ((TextView)findViewById(viewId)).setText(text);
    }

    public void editTable(View view) {
        String fullName = getResources().getResourceName(view.getId());
        String name = fullName.substring(fullName.lastIndexOf("/") + 1);
        String thu = name.substring(3,5);
        int ca = Integer.parseInt(name.substring(6));
        System.out.println(thu+" Ca "+ca);
        Toast.makeText(ScheduleActivity.this,thu+" Ca "+ca,Toast.LENGTH_LONG).show();

        saveThu = weekday.indexOf(thu);
        saveCa = ca;
        ApiClient.getAPI().getSchedulebyDayandShift(AccountManager.getUid(),saveThu,saveCa).enqueue(new Callback<ScheduleItem>() {
            @Override
            public void onResponse(Call<ScheduleItem> call, Response<ScheduleItem> response) {
                ScheduleItem scheduleItem = response.body();
                System.out.println(scheduleItem);
                if(scheduleItem==null){
                    ScheduleDialogFragment dialog
                            = new ScheduleDialogFragment();
                    dialog.show(getFragmentManager(),
                            "Schedule Dialog");
                }
                else {
                    ScheduleDialogFragment dialog
                            = new ScheduleDialogFragment(scheduleItem.getSubject(),scheduleItem.getNote());
                    dialog.show(getFragmentManager(),
                            "Schedule Dialog");
                }
            }

            @Override
            public void onFailure(Call<ScheduleItem> call, Throwable t) {

            }
        });
    }

    @Override
    public void sendInput(String subjectText, String noteText) {

        ScheduleItem scheduleItem = new ScheduleItem();
        scheduleItem.setWeekday(saveThu);
        scheduleItem.setShift(saveCa);
        scheduleItem.setSubject(subjectText);
        scheduleItem.setNote(noteText);
        if (scheduleItem.getSubject().trim().equals("")&&scheduleItem.getNote().trim().equals("")){
            ScheduleManager.cancelScheduleNoti(ScheduleActivity.this,scheduleItem);
        }
        else {
            ScheduleManager.scheduleNoti(ScheduleActivity.this,scheduleItem);
        }
        ApiClient.getAPI().updateUserByID(AccountManager.getUid(),scheduleItem).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.println(response.body());
                loadData();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                loadData();
            }
        });
    }

    public void stopDaily(View view) {
        ScheduleManager.cancelDailyNoti(this);
    }
}