package com.example.questans.broadcastreceiver;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlarmManager;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.example.questans.data.ApiClient;
import com.example.questans.model.ScheduleItem;
import com.example.questans.service.RescheduleService;
import com.example.questans.service.ScheduleService;
import com.example.questans.user.AccountManager;
import com.example.questans.utils.ScheduleManager;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ScheduleBroadcastReceiver extends BroadcastReceiver {
//    AlarmModel alarmModel;
    public static final String MONDAY = "MONDAY";
    public static final String TUESDAY = "TUESDAY";
    public static final String WEDNESDAY = "WEDNESDAY";
    public static final String THURSDAY = "THURSDAY";
    public static final String FRIDAY = "FRIDAY";
    public static final String SATURDAY = "SATURDAY";
    public static final String SUNDAY = "SUNDAY";
    public static final String SUBJECT = "SUBJECT";
    public static final String NOTE = "NOTE";
    public static final String DAILY = "DAILY";
    private SharedPreferences mPreferences;
    private String sharedPrefFile = "MainPref";


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("An_Test","AlarmBroadcastReceiver: "+intent.toString());
        System.out.println("Broadcast!");
        mPreferences = context.getSharedPreferences(sharedPrefFile,MODE_PRIVATE);
        String uidPref = mPreferences.getString ("uid_pref",null);
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
//            String toastText = String.format("Alarm Reboot");
//            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
            startRescheduleService(uidPref, context);
        }
        else {
//            String toastText = String.format("Alarm Received");
//            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
            if (intent.getBooleanExtra(DAILY,false)){
//                ScheduleManager.scheduleAllNoti(context);
                startDailyScheduleService(uidPref, context, intent);
            }
            else {
                startScheduleService(context, intent);
            }
        }
    }

    private boolean alarmIsToday(Intent intent) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int today = calendar.get(Calendar.DAY_OF_WEEK);

        switch(today) {
            case Calendar.MONDAY:
                if (intent.getBooleanExtra(MONDAY, false))
                    return true;
                return false;
            case Calendar.TUESDAY:
                if (intent.getBooleanExtra(TUESDAY, false))
                    return true;
                return false;
            case Calendar.WEDNESDAY:
                if (intent.getBooleanExtra(WEDNESDAY, false))
                    return true;
                return false;
            case Calendar.THURSDAY:
                if (intent.getBooleanExtra(THURSDAY, false))
                    return true;
                return false;
            case Calendar.FRIDAY:
                if (intent.getBooleanExtra(FRIDAY, false))
                    return true;
                return false;
            case Calendar.SATURDAY:
                if (intent.getBooleanExtra(SATURDAY, false))
                    return true;
                return false;
            case Calendar.SUNDAY:
                if (intent.getBooleanExtra(SUNDAY, false))
                    return true;
                return false;
        }
        return false;
    }

    private void startScheduleService(Context context, Intent intent) {
        System.out.println("Schedule");
        Intent intentService = new Intent(context, ScheduleService.class);
        intentService.putExtra(SUBJECT, intent.getStringExtra(SUBJECT));
        intentService.putExtra(NOTE, intent.getStringExtra(NOTE));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        } else {
            context.startService(intentService);
        }
    }

    private void startDailyScheduleService(String uid, Context context, Intent intent) {
        System.out.println("Daily");
        Calendar calendar = Calendar.getInstance();
        System.out.println(calendar.get(Calendar.DAY_OF_WEEK));
        ApiClient.getAPI().getSchedulebyDay(uid,calendar.get(Calendar.DAY_OF_WEEK)).enqueue(new Callback<List<ScheduleItem>>() {
            @Override
            public void onResponse(Call<List<ScheduleItem>> call, Response<List<ScheduleItem>> response) {
                List<ScheduleItem> scheduleItemList = response.body();
                String notice = "";
                Collections.sort(scheduleItemList, new Comparator<ScheduleItem>() {
                    @Override
                    public int compare(ScheduleItem scheduleItem, ScheduleItem t1) {
                        if(scheduleItem.getShift()>t1.getShift()) return 1;
                        else if(scheduleItem.getShift()==t1.getShift()) return 0;
                        else return -1;
                    }
                });
                for (ScheduleItem s:scheduleItemList) {
                    System.out.println(s.getShift()+" ");
                    notice+="Ca "+s.getShift()+": "+s.getSubject();
                    if(s.getNote()!=null&&!s.getNote().trim().equals(""))
                        notice+=" - "+s.getNote();
                    notice+="\n";
                }
                Intent intentService = new Intent(context, ScheduleService.class);
                intentService.putExtra(NOTE,notice);
                intentService.putExtra(DAILY, true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(intentService);
                } else {
                    context.startService(intentService);
                }
            }

            @Override
            public void onFailure(Call<List<ScheduleItem>> call, Throwable t) {

            }
        });
    }

    private void startRescheduleService(String uid, Context context) {
        System.out.println("Reboot");
        Intent intentService = new Intent(context, RescheduleService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        } else {
            context.startService(intentService);
        }
    }
}