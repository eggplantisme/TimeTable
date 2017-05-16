package com.example.admin.timetable;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by admin on 2016/12/10.
 */
public class DetailActivity extends AppCompatActivity {
    Course course = new Course();
    int week;
    int start;
    TimerService timerService = MainActivity.timerService;
    String _ddl;
    String _ddl_time;
    String _name;
    TextView name;
    TextView ddl;
    TextView endTime;
    Button timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memo);
        Bundle receiveBundle = getIntent().getExtras();
        week = receiveBundle.getInt("week");
        start = receiveBundle.getInt("start");
        Button sure = (Button)findViewById(R.id.sure);
        Button edit = (Button)findViewById(R.id.edit);
        Button delete = (Button)findViewById(R.id.delete);
        timer = (Button)findViewById(R.id.timer);

        name = (TextView) findViewById(R.id.name);
        ddl = (TextView)findViewById(R.id.ddl);
        endTime = (TextView)findViewById(R.id.endDateTime);




        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("edit", "yes");
                Intent intent = new Intent(DetailActivity.this, EditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("course", course);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBase dataBase = new DataBase(DetailActivity.this, "my.db", null, 1);
                dataBase.delete(week, start);
                Toast.makeText(DetailActivity.this, "删除成功", Toast.LENGTH_SHORT).show();

                Intent i = new Intent("WIDGET_UPDATE");
                sendBroadcast(i);
                Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                startActivity(intent);

                MainActivity.timerService.cancel_Timer(_ddl_time, _ddl, _name);
            }
        });
        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timer.getText().toString().equals("开启定时功能")) {

                    //绑定定时服务
                    if (_ddl_time.equals("") || _ddl.equals("")) {
                        Log.v("open", "null");
                    }
                    else {
                        timer.setText("关闭定时功能");
                        Log.v("open", "yes");
                        MainActivity.timerService.timer(_ddl_time, _ddl, _name);
                        /*
                        Intent intent = new Intent("DAY");
                        Bundle bundle = new Bundle();
                        bundle.putString("day", _ddl_time);
                        bundle.putString("content", _ddl);
                        bundle.putString("course", _name);
                        intent.putExtras(bundle);
                        sendBroadcast(intent);*/
                    }
                } else if (timer.getText().toString().equals("关闭定时功能")) {
                    timer.setText("开启定时功能");
                    MainActivity.timerService.cancel_Timer(_ddl_time, _ddl, _name);
                    Log.v("open", "no");
                }
                Log.v("open", "meaningness");
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:


                        DataBase dataBase = new DataBase(DetailActivity.this, "my.db", null, 1);
                        course = dataBase.query(week, start);
                        TextView name = (TextView)findViewById(R.id.name);
                        TextView information = (TextView)findViewById(R.id.information);
                        TextView ddl = (TextView)findViewById(R.id.ddl);
                        TextView endTime = (TextView)findViewById(R.id.endDateTime);


                        name.setText(course.getName());
                        information.setText("星期：" + course.getWeek() + "\n"
                                + "课节：" + course.getStart() + "~" + course.getEnd() + "\n"
                                + "教室：" + course.getAddress() + "\n"
                                + "课程资料：" + course.getEmail_ftp() + "\n"
                                + "老师：" + course.getTeacher());
                        ddl.setText(course.getDdl());
                        endTime.setText(course.getDdl_time());
                        //获取timer服务需要的信息，名字，作业，时间
                        _ddl = ddl.getText().toString();
                        _ddl_time = endTime.getText().toString();
                        _name = name.getText().toString();
                        String timer_information = _name + " " + _ddl_time + " " + _ddl;
                        if (MainActivity.timerService.query_timers(timer_information)) {
                            timer.setText("关闭定时功能");
                        } else {
                            timer.setText("开启定时功能");
                        }
                        break;
                    default:
                        break;
                }
            }
        };
        Message msg = Message.obtain(handler);
        msg.what = 1;
        msg.sendToTarget();
    }
}
