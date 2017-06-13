package com.example.admin.timetable;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 2016/12/11.
 */
public class WidgetDemo  extends AppWidgetProvider {

    private ComponentName thisWidget;
    private RemoteViews remoteViews;
    private int index = 0;

    public static final String PREV_ONCLICK = "PREV_BUTTON_ONCLICK";
    public static final String NEXT_ONCLICK = "NEXT_BUTTON_ONCLICK";

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

        switch (intent.getAction()) {
            case PREV_ONCLICK:
                index = index - 1 < 0 ? 0 : index - 1;
                break;

            case NEXT_ONCLICK:
                index++;
                break;
        }

        update(context);
    }

    private void update(Context context) {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);

        Intent clickInt = new Intent(context, MainActivity.class);
        Intent clickPrev = new Intent(PREV_ONCLICK);
        Intent clickNext = new Intent(NEXT_ONCLICK);

        PendingIntent pi = PendingIntent.getActivity(context, 0, clickInt, 0);
        PendingIntent piPrev = PendingIntent.getBroadcast(context, 1, clickPrev, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent piNext = PendingIntent.getBroadcast(context, 1, clickNext, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_demo);
        ComponentName cn = new ComponentName(context, WidgetDemo.class);

        views.setOnClickPendingIntent(R.id.content_layout, pi);
        views.setOnClickPendingIntent(R.id.prev, piPrev);
        views.setOnClickPendingIntent(R.id.next, piNext);

        String ddlContent = "Content";
        String ddlClass = "Class";
        String ddlTime = "Time";

        if (!getCourseDdlContent(index).equals(""))
        {
            ddlContent = getCourseDdlContent(index);
            ddlClass = getCourseDdlClass(index);
            ddlTime = getCourseDdlTime(index);
        }

        views.setTextViewText(R.id.ddl_content, ddlContent);
        views.setTextViewText(R.id.ddl_class, ddlClass);
        views.setTextViewText(R.id.ddl_time, ddlTime);
        if (manager != null) {
            manager.updateAppWidget(manager.getAppWidgetIds(cn), views);
        }
    }

    private String getCourseDdlTime(int i) {
        DataBase dataBase = MainActivity.dataBase;
        Cursor cursor = dataBase.query_all();

        String ddl_time = "";
        int count = 0;

        if (cursor.moveToFirst())
        {
            if (count == i)
                return cursor.getString(cursor.getColumnIndex("ddl_time"));

            ddl_time = cursor.getString(cursor.getColumnIndex("ddl_time"));
            while (cursor.moveToNext())
            {
                if (++count == i)
                {
                    if (!cursor.getString(cursor.getColumnIndex("ddl_time")).equals(""))
                        ddl_time = cursor.getString(cursor.getColumnIndex("ddl_time"));

                    break;
                }
            }
        }

        return ddl_time;
    }

    private String getCourseDdlContent(int i) {
        DataBase dataBase = MainActivity.dataBase;
        Cursor cursor = dataBase.query_all();

        String ddl_content = "";
        int count = 0;

        if (cursor.moveToFirst())
        {
            if (count == i)
                return cursor.getString(cursor.getColumnIndex("ddl"));

            ddl_content = cursor.getString(cursor.getColumnIndex("ddl"));
            while (cursor.moveToNext())
            {
                if (++count == i)
                {
                    if (!cursor.getString(cursor.getColumnIndex("ddl")).equals(""))
                        ddl_content = cursor.getString(cursor.getColumnIndex("ddl"));

                    break;
                }
            }
        }

        return ddl_content;
    }

    private String getCourseDdlClass(int i) {
        DataBase dataBase = MainActivity.dataBase;
        Cursor cursor = dataBase.query_all();

        String ddl_class = "";
        int count = 0;

        if (cursor.moveToFirst())
        {
            if (count == i)
                return cursor.getString(cursor.getColumnIndex("name"));

            ddl_class = cursor.getString(cursor.getColumnIndex("name"));
            while (cursor.moveToNext())
            {
                if (++count == i)
                {
                    if (!cursor.getString(cursor.getColumnIndex("name")).equals(""))
                        ddl_class = cursor.getString(cursor.getColumnIndex("name"));

                    break;
                }
            }
        }

        return ddl_class;
    }
}
