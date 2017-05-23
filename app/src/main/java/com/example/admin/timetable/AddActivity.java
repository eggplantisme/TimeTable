package com.example.admin.timetable;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by admin on 2016/12/8.
 */
public class AddActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        final EditText name = (EditText)findViewById(R.id.name);
        final EditText teacher = (EditText)findViewById(R.id.teacher);
        final EditText email_ftp = (EditText)findViewById(R.id.email_ftp);
        final EditText week = (EditText)findViewById(R.id.week);
        final EditText start = (EditText)findViewById(R.id.start);
        final EditText end = (EditText)findViewById(R.id.end);
        final EditText address = (EditText)findViewById(R.id.address);
        Bundle receiveBundle = getIntent().getExtras();
        start.setText(receiveBundle.getInt("start") + "");
        Button submit = (Button)findViewById(R.id.submit);
        final DataBase dataBase = new DataBase(AddActivity.this, "my.db", null, 1);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _name = name.getText().toString();
                String _teacher = teacher.getText().toString();
                String _email_ftp = email_ftp.getText().toString();
                String _address = address.getText().toString();
                int _week = 0;
                int _start = 0;
                int _end = 0;
                if (week.getText().toString().equals("") || start.getText().toString().equals("") || end.getText().toString().equals("")) {
                    Toast.makeText(AddActivity.this, "请输入周数开始课节和结束课节", Toast.LENGTH_SHORT).show();
                } else {
                    _week = Integer.parseInt(week.getText().toString());
                    _start = Integer.parseInt(start.getText().toString());
                    _end = Integer.parseInt(end.getText().toString());
                    if (_end < _start) {
                        Toast.makeText(AddActivity.this, "结束课节应该大于开始课节", Toast.LENGTH_SHORT).show();
                    } else if (_week < 1 || _week > 7) {
                        Toast.makeText(AddActivity.this, "周数应该在1-7之间", Toast.LENGTH_SHORT).show();
                    } else if (_start < 1 || _start > 16 || _end > 16 || _end < 1) {
                        Toast.makeText(AddActivity.this, "课节数应该在1-16之间", Toast.LENGTH_SHORT).show();
                    } else if (dataBase.query_same(_week, _start, _end)) {
                        Toast.makeText(AddActivity.this, "该时间段有重复的课程", Toast.LENGTH_SHORT).show();
                    } else {
                        Course course = new Course();
                        course.setName(_name);
                        course.setTeacher(_teacher);
                        course.setEmail_ftp(_email_ftp);
                        course.setWeek(_week);
                        course.setStart(_start);
                        course.setEnd(_end);
                        course.setAddress(_address);
                        dataBase.insert(course);
                        Intent intent = new Intent(AddActivity.this, MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("course", course);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            }
        });
    }
}
