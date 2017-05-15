package com.example.admin.timetable;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by admin on 2016/12/11.
 */
public class TimerService extends Service{
    private final IBinder binder = new MyBinder();
    public class MyBinder extends Binder {
        TimerService getService() {
            return TimerService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class timer_bind {
        public Timer timer;
        public String information;
        timer_bind(Timer t, String in) {
            timer = t;
            information = in;
        }
    }
    public ArrayList<timer_bind> timers = new ArrayList<>();
    public boolean query_timers(String _information) {
        for (int i = 0; i < timers.size(); i++) {
            if (timers.get(i).information.equals(_information)) {
                return true;
            }
        }
        return false;
    }
    private Timer _timer;
    public void timer(String end, final String ddl, final String name) {
        String timer_information = name +" " + end + " " + ddl;
        if (query_timers(timer_information)) {}
        else {
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日HH:mm");
            try {
                final Date endDate = simpleDateFormat.parse(end);
                _timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        //格式化当前时间
                        Date currDate = new Date(System.currentTimeMillis());
                        long day = (endDate.getTime() - currDate.getTime()) / (24 * 60 *60 * 1000);
                        Intent intent = new Intent("DAY");
                        Bundle bundle = new Bundle();
                        bundle.putString("day", day + "");
                        bundle.putString("content", ddl);
                        bundle.putString("course", name);
                        intent.putExtras(bundle);
                        sendBroadcast(intent);
                        Log.v("notification", day + "");
                    }
                };
                _timer.schedule(task, 0, 10*1000);
                timers.add(new timer_bind(_timer, timer_information));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void cancel_Timer(String end, final String ddl, final String name) {
        String timer_information = name +" " + end + " " + ddl;
        if (query_timers(timer_information)) {
            for (int i = 0; i < timers.size(); i++) {
                if (timers.get(i).information.equals(timer_information)) {
                    timers.get(i).timer.cancel();
                    timers.remove(i);
                }
            }
        }
    }

}
