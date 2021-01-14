package com.msu.todolist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Calendar;

public class RefreshNotifications extends BroadcastReceiver {

    DatabaseHelper dbHelper;
    ArrayList<Task> allTasks;
    int [] DateArr = new int[5];

    @Override
    public void onReceive(Context context, Intent intent) {

        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            dbHelper = new DatabaseHelper(context);
            allTasks .addAll(TaskSys.plannedTaskList(TaskDB.getAllTasks(dbHelper)));
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent notificationIntent = new Intent(context, NotificationReceiver.class);

            for(int i = 0; i < allTasks.size(); i++){
                    Task tsk = allTasks.get(i);
                    DateArr = DateConverter.splitTaskDateTime(tsk.getDate(), tsk.getTime());

                    long id = (long) tsk.getId();
                    int mYear =  DateArr[0];
                    int mMonth =  DateArr[1];
                    int mDay = DateArr[2];
                    int mHour =  DateArr[3];
                    int mMin =  DateArr[4];

                    PendingIntent broadcast = PendingIntent.getBroadcast(context, (int) id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    Calendar calendar= Calendar.getInstance();
                    calendar.set(mYear, mMonth, mDay, mHour, mMin, 00);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), broadcast);
                }
            }

        }
    }