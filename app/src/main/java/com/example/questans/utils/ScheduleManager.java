package com.example.questans.utils;

import static android.content.Context.MODE_PRIVATE;
import static com.example.questans.broadcastreceiver.ScheduleBroadcastReceiver.DAILY;
import static com.example.questans.broadcastreceiver.ScheduleBroadcastReceiver.NOTE;
import static com.example.questans.broadcastreceiver.ScheduleBroadcastReceiver.SUBJECT;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.questans.broadcastreceiver.ScheduleBroadcastReceiver;
import com.example.questans.data.ApiClient;
import com.example.questans.model.ScheduleItem;

import java.sql.Time;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleManager {
    static final long RUN_DAILY = 24 * 60 * 60 * 1000;
    static final long RUN_WEEKLY = 7 * 24 * 60 * 60 * 1000;
    private static SharedPreferences mPreferences;
    private static String sharedPrefFile = "MainPref";
    public static void dailyNoti(Context context){
        cancelDailyNoti(context);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ScheduleBroadcastReceiver.class);
        intent.putExtra(DAILY, true);
//        context.sendBroadcast(intent);
        PendingIntent dailyPendingIntent = PendingIntent.getBroadcast(context,0,intent,intent.FILL_IN_DATA);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR_OF_DAY, 21);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MILLISECOND, 0);

        calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND)+10);

        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        }
        System.out.println(calendar.get(Calendar.DAY_OF_MONTH)+"/"+(calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.YEAR));
        System.out.println(calendar.get(Calendar.HOUR)+":"+calendar.get(Calendar.MINUTE)+":"+calendar.get(Calendar.SECOND));
//        alarmManager.setRepeating(
//                AlarmManager.RTC_WAKEUP,
//                calendar.getTimeInMillis(),
//                RUN_DAILY,
//                dailyPendingIntent
//        );
        alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                dailyPendingIntent
        );
        System.out.println("DAILY!");
    }

    public static void cancelDailyNoti(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ScheduleBroadcastReceiver.class);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager.cancel(alarmPendingIntent);

        System.out.println("Cancel!");
    }
    public static void scheduleAllNoti(Context context){
        mPreferences = context.getSharedPreferences(sharedPrefFile,MODE_PRIVATE);
        String uidPref = mPreferences.getString ("uid_pref",null);
        ApiClient.getAPI().getAllSchedules(uidPref).enqueue(new Callback<List<ScheduleItem>>() {
            @Override
            public void onResponse(Call<List<ScheduleItem>> call, Response<List<ScheduleItem>> response) {
                List<ScheduleItem> scheduleItemList=response.body();
                for (ScheduleItem s: scheduleItemList) {
                    scheduleNoti(context,s);
                }
            }

            @Override
            public void onFailure(Call<List<ScheduleItem>> call, Throwable t) {

            }
        });
    }

    public static void cancleAllNoti(Context context){
        ScheduleItem scheduleItem = new ScheduleItem();
        for (int i = 1; i <=7; i++) {
            for (int j = 1; j <= 10 ; j++) {
                scheduleItem.setWeekday(i);
                scheduleItem.setShift(j);
                cancelScheduleNoti(context,scheduleItem);
            }
        }
    }

    public static void scheduleNoti(Context context, ScheduleItem scheduleItem){
        cancelScheduleNoti(context,scheduleItem);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ScheduleBroadcastReceiver.class);
        intent.putExtra(SUBJECT,scheduleItem.getSubject());
        intent.putExtra(NOTE,scheduleItem.getNote());
//        context.sendBroadcast(intent);
        PendingIntent dailyPendingIntent = PendingIntent.getBroadcast(context,toCode(scheduleItem),intent,intent.FILL_IN_DATA);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        Time time = shiftTime(scheduleItem.getShift());
        calendar.set(Calendar.DAY_OF_WEEK, scheduleItem.getWeekday());
        calendar.set(Calendar.HOUR_OF_DAY, time.getHours());
        calendar.set(Calendar.MINUTE, time.getMinutes());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 7);
        }
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                RUN_WEEKLY,
                dailyPendingIntent
        );
//        alarmManager.setExact(
//                AlarmManager.RTC_WAKEUP,
//                calendar.getTimeInMillis(),
//                dailyPendingIntent
//        );
        System.out.println("WEEKLY! "+toCode(scheduleItem));
    }
    public static void cancelScheduleNoti(Context context, ScheduleItem scheduleItem){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ScheduleBroadcastReceiver.class);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, toCode(scheduleItem), intent, 0);
        alarmManager.cancel(alarmPendingIntent);

        System.out.println("Cancel! "+toCode(scheduleItem));
    }
    private static int toCode(ScheduleItem scheduleItem){
        return scheduleItem.getShift()*10+scheduleItem.getWeekday();
    }
    private static int getWeekday(int code){
        return code%10;
    }
    private static int getShift(int code){
        return code/10;
    }

    public static Time shiftTime(int shift){
        int hour, minute;
        switch (shift){
            case 1:
                hour=7;
                minute=0;
                break;
            case 2:
                hour=8;
                minute=15;
                break;

            case 3:
                hour=9;
                minute=30;
                break;

            case 4:
                hour=10;
                minute=45;
                break;

            case 5:
                hour=13;
                minute=0;
                break;

            case 6:
                hour=14;
                minute=15;
                break;

            case 7:
                hour=15;
                minute=30;
                break;

            case 8:
                hour=16;
                minute=45;
                break;

            case 9:
                hour=20;
                minute=0;
                break;

            case 10:
                hour=21;
                minute=0;
                break;
            default:
                hour=6;
                minute=0;
                break;
        }
        return new Time(hour,minute,0);
    }
}
