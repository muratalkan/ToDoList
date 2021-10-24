package com.msu.todolist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Calendar;
import com.msu.todolist.NotificationHelper;

public class RefreshNotifications extends BroadcastReceiver {

    DatabaseHelper dbHelper;
    ArrayList<Task> allTasks;
    int [] DateArr = new int[5];

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            NotificationHelper notificationHelper = new NotificationHelper(context);
            dbHelper = new DatabaseHelper(context);
            allTasks = TaskSys.plannedTaskList(TaskDB.getAllTasks(dbHelper));

            for(int i = 0; i < allTasks.size(); i++){
                Task tsk = allTasks.get(i);
                DateArr = DateConverter.splitTaskDateTime(tsk.getDate(), tsk.getTime());

                long id = (long) tsk.getId();
                Calendar task_calendar = Calendar.getInstance();
                task_calendar.set(DateArr[2], DateArr[1], DateArr[0], DateArr[3], DateArr[4], 00);
                notificationHelper.createNotification(id, task_calendar);
            }
        }
    }
}