package com.example.admin.timetable;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Created by admin on 2016/12/11.
 */
public class WidgetDemo  extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        update(context);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals("WIDGET_UPDATE")) {
            Log.v("widget", "yes");
            update(context);
        }
    }
    private void update(Context context) {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        Intent clickInt = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, clickInt, 0);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_demo);
        ComponentName cn = new ComponentName(context, WidgetDemo.class);
        views.setOnClickPendingIntent(R.id.ddl_content, pi);
        views.setTextViewText(R.id.ddl_content, getCourseDdl());
        if (manager != null) {
            manager.updateAppWidget(manager.getAppWidgetIds(cn), views);
        }
    }

    private String getCourseDdl() {
        DataBase dataBase = MainActivity.dataBase;
        Cursor cursor = dataBase.query_all();
        String ddl_content = "";
        if (cursor.moveToFirst() == false) {}
        else {
            if (!cursor.getString(cursor.getColumnIndex("ddl")).equals("")) {
                ddl_content += cursor.getString(cursor.getColumnIndex("name")) + "\n" + cursor.getString(cursor.getColumnIndex("ddl")) + " : " + cursor.getString(cursor.getColumnIndex("ddl_time")) + "\n";
            }
                while (cursor.moveToNext()) {
                    if (!cursor.getString(cursor.getColumnIndex("ddl")).equals("")) {
                        ddl_content += cursor.getString(cursor.getColumnIndex("name")) + "\n" + cursor.getString(cursor.getColumnIndex("ddl")) + " : " + cursor.getString(cursor.getColumnIndex("ddl_time")) + "\n";
                    }
                }
        }
        return ddl_content;
    }
}
