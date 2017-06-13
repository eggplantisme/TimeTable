package com.example.admin.timetable;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Vibrator;

/**
 * Created by admin on 2016/12/11.
 */
public class DayReceiver  extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("DAY")) {
            Bundle bundle = intent.getExtras();
            String day = bundle.getString("day");
            String course = bundle.getString("course");
            String content = bundle.getString("content");
            String detail_content;
            if (Integer.parseInt(day) == 0) {
                MainActivity.vibrator.vibrate(1000);
                detail_content = course + "\n" + "内容：" + content + "\n还有1天";
                int largeIcon = R.mipmap.ic_launcher;
                Notification.Builder builder = new Notification.Builder(context);
                builder.setContentTitle("DDL")
                        .setContentText(detail_content)
                        .setTicker("又来了一个DDL")
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), largeIcon))
                        .setSmallIcon(largeIcon)
                        .setAutoCancel(true);
                NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

                Intent back = new Intent(context, MainActivity.class);
                PendingIntent goBack = PendingIntent.getActivity(context, 0, back, 0);
                builder.setContentIntent(goBack);

                Notification notify = builder.build();
                manager.notify(0, notify);
            }
        }
    }
}
