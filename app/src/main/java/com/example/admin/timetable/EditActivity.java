package com.example.admin.timetable;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by admin on 2016/12/10.
 */
public class EditActivity extends AppCompatActivity {
    private EditText endDateTime;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        final EditText name = (EditText)findViewById(R.id.name);
        final EditText teacher = (EditText)findViewById(R.id.teacher);
        final EditText email_ftp = (EditText)findViewById(R.id.email_ftp);
        final TextView week = (TextView) findViewById(R.id.week);
        final TextView start = (TextView)findViewById(R.id.start);
        final TextView end = (TextView)findViewById(R.id.end);
        final EditText address = (EditText)findViewById(R.id.address);
        final EditText ddl = (EditText)findViewById(R.id.ddl);
        final EditText ddl_time = (EditText)findViewById(R.id.endDateTime);
        //获取课程
        Bundle receiveBundle = getIntent().getExtras();
        final Course course = (Course)receiveBundle.getSerializable("course");

        //截止时间
        endDateTime = (EditText)findViewById(R.id.endDateTime);
        endDateTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DateTimePickDialogUtil dateTimePickDialogUtil = new DateTimePickDialogUtil(EditActivity.this, "");
                    dateTimePickDialogUtil.dateTimePicKDialog(endDateTime);
                    endDateTime.setEnabled(false);
                } else {
                    endDateTime.setEnabled(true);
                }
            }
        });

        Button clear = (Button)findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ddl_time.setText("");
            }
        });

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        name.setText(course.getName());
                        teacher.setText(course.getTeacher());
                        email_ftp.setText(course.getEmail_ftp());
                        week.setText(course.getWeek() + "");
                        start.setText(course.getStart() + "");
                        end.setText(course.getEnd() + "");
                        address.setText(course.getAddress());
                        ddl.setText(course.getDdl());
                        ddl_time.setText(course.getDdl_time());
                }
            }
        };
        Message msg = Message.obtain(handler);
        msg.what = 1;
        msg.sendToTarget();
        //提交
        Button submit = (Button)findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBase dataBase = new DataBase(EditActivity.this, "my.db", null, 1);
                String _name = name.getText().toString();
                String _teacher = teacher.getText().toString();
                String _email_ftp = email_ftp.getText().toString();
                String _address = address.getText().toString();
                int _week = 0;
                int _start = 0;
                int _end = 0;
                String _ddl = ddl.getText().toString();
                String _ddl_time = ddl_time.getText().toString();
                if (week.getText().toString().equals("") || start.getText().toString().equals("") || end.getText().toString().equals("")) {
                    Toast.makeText(EditActivity.this, "请输入周数开始课节和结束课节", Toast.LENGTH_SHORT).show();
                } else {
                    _week = Integer.parseInt(week.getText().toString());
                    _start = Integer.parseInt(start.getText().toString());
                    _end = Integer.parseInt(end.getText().toString());
                    if (_end < _start) {
                        Toast.makeText(EditActivity.this, "结束课节应该大于开始课节", Toast.LENGTH_SHORT).show();
                    } else if (_week < 1 || _week > 7) {
                        Toast.makeText(EditActivity.this, "周数应该在1-7之间", Toast.LENGTH_SHORT).show();
                    } else if (_start < 1 || _start > 16 || _end > 16 || _end < 1) {
                        Toast.makeText(EditActivity.this, "课节数应该在1-16之间", Toast.LENGTH_SHORT).show();
                    } else {
                        Course course = new Course();
                        course.setName(_name);
                        course.setTeacher(_teacher);
                        course.setEmail_ftp(_email_ftp);
                        course.setWeek(_week);
                        course.setStart(_start);
                        course.setEnd(_end);
                        course.setAddress(_address);
                        course.setDdl(_ddl);
                        course.setDdl_time(_ddl_time);
                        dataBase.update(course);
                        Intent intent = new Intent(EditActivity.this, MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("course", course);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
                Intent intent = new Intent("WIDGET_UPDATE");
                sendBroadcast(intent);
            }
        });
    }
}
