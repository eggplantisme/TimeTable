package com.example.admin.timetable;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {
    private int gridHeight, gridWidth;
    private RelativeLayout layout;
    private RelativeLayout MonLayout;
    private static boolean isFirst = true;
    private static boolean course_color = true;
    public Handler handler;
    static public DataBase dataBase;
    static public Vibrator vibrator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MonLayout = (RelativeLayout)findViewById(R.id.mon);
        gridWidth = MonLayout.getWidth();
        gridHeight = MonLayout.getHeight() / 16;
        layout = MonLayout;
        dataBase = new DataBase(this, "my.db", null, 1);
        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        //获取到宽高的时候更新页面
        MonLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (isFirst) {
                    isFirst = false;
                    gridWidth = MonLayout.getWidth();
                    gridHeight = MonLayout.getHeight() / 16;
                    Log.v("width", gridWidth + "");
                    Log.v("height", gridHeight + "");
                    Message message = Message.obtain(handler);
                    message.what = 1;
                    message.sendToTarget();
                }
                return true;
            }
        });
        //绑定服务
        Intent intent = new Intent(this, TimerService.class);
        bindService(intent, sc, Context.BIND_AUTO_CREATE);
    }
    @Override
    protected void onResume() {
        super.onResume();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        dataBase = new DataBase(MainActivity.this, "my.db", null, 1);
                        update_layout();
                        break;
                    case 2:
                        registerCourseAddButton();
                    default:
                        break;
                }
            }
            private void update_layout() {
                clearLayout();
                Cursor cursor = dataBase.query_all();
                if (cursor.moveToFirst() == false) {Log.v("cursor", "false");}
                else {
                    Course course = new Course();
                    course.setName(cursor.getString(cursor.getColumnIndex("name")));
                    course.setTeacher(cursor.getString(cursor.getColumnIndex("teacher")));
                    course.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                    course.setEmail_ftp(cursor.getString(cursor.getColumnIndex("email_ftp")));
                    course.setDdl(cursor.getString(cursor.getColumnIndex("ddl")));
                    course.setStart(cursor.getInt(cursor.getColumnIndex("start")));
                    course.setEnd(cursor.getInt(cursor.getColumnIndex("end")));
                    course.setWeek(cursor.getInt(cursor.getColumnIndex("week")));
                    Log.v("coursename", course.getName());
                    addCourse(course);
                    while (cursor.moveToNext()) {
                        course.setName(cursor.getString(cursor.getColumnIndex("name")));
                        course.setTeacher(cursor.getString(cursor.getColumnIndex("teacher")));
                        course.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                        course.setEmail_ftp(cursor.getString(cursor.getColumnIndex("email_ftp")));
                        course.setDdl(cursor.getString(cursor.getColumnIndex("ddl")));
                        course.setStart(cursor.getInt(cursor.getColumnIndex("start")));
                        course.setEnd(cursor.getInt(cursor.getColumnIndex("end")));
                        course.setWeek(cursor.getInt(cursor.getColumnIndex("week")));
                        Log.v("coursename", course.getName());
                        addCourse(course);
                    }
                    Log.v("cursor", "true");
                }
            }
            private void clearLayout() {
                layout = (RelativeLayout) findViewById(R.id.mon);
                clear_view(layout);
                layout = (RelativeLayout) findViewById(R.id.tue);
                clear_view(layout);
                layout = (RelativeLayout) findViewById(R.id.wed);
                clear_view(layout);
                layout = (RelativeLayout) findViewById(R.id.thr);
                clear_view(layout);
                layout = (RelativeLayout) findViewById(R.id.fri);
                clear_view(layout);
                layout = (RelativeLayout) findViewById(R.id.sat);
                clear_view(layout);
                layout = (RelativeLayout) findViewById(R.id.sun);
                clear_view(layout);
            }
            private void clear_view(RelativeLayout layout) {
                int count = layout.getChildCount();
                for (int i = 0; i < count; i++) {
                    View view = layout.getChildAt(i);
                    layout.removeView(view);
                }
            }

            private Button createCourseButton(int start, int end, String text, int week, String addresss) {
                Button course = new Button(MainActivity.this);
                //设定宽度和高度
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gridWidth, gridHeight * (end - start + 1));
                //设定位置
                course.setY(gridHeight * (start - 1));
                course.setLayoutParams(params);
                course.setGravity(Gravity.CENTER);
                if (course_color) {
                    course.setBackgroundColor(Color.parseColor("#CDE6C7"));
                    course_color = false;
                } else  {
                    course_color = true;
                    course.setBackgroundColor(Color.parseColor("#ABC3DC"));
                }
                course.setText(text + "\n" + "时间：" + start + "~" + end + "\n" + "教室：" + addresss);
                course.setTextSize(10);
                course.setTag(week);
                course.setOnClickListener(courseOnClickListener);
                layout.addView(course, params);
                Log.v("add", "yes");
                layout.invalidate();
                return course;
            }
            private View.OnClickListener courseOnClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int start = (int)(v.getY() / gridHeight) + 1;
                    int week = (int)v.getTag();
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("start", start);
                    bundle.putInt("week", week);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            };

            private void addCourse(Course course) {
                Button course_to_add;
                switch (course.getWeek()) {
                    case 1:
                        layout = (RelativeLayout) findViewById(R.id.mon);
                        break;
                    case 2:
                        layout = (RelativeLayout) findViewById(R.id.tue);
                        break;
                    case 3:
                        layout = (RelativeLayout) findViewById(R.id.wed);
                        break;
                    case 4:
                        layout = (RelativeLayout) findViewById(R.id.thr);
                        break;
                    case 5:
                        layout = (RelativeLayout) findViewById(R.id.fri);
                        break;
                    case 6:
                        layout = (RelativeLayout) findViewById(R.id.sat);
                        break;
                    case 7:
                        layout = (RelativeLayout) findViewById(R.id.sun);
                        break;
                }
                course_to_add = createCourseButton(course.getStart(), course.getEnd(), course.getName(), course.getWeek(), course.getAddress());
            }
        };
        //更新页面
        Message message = Message.obtain(handler);
        message.what = 1;
        message.sendToTarget();
        //注册button
        Message msg = Message.obtain(handler);
        msg.what = 2;
        msg.sendToTarget();
    }


    /*@Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Course course = (Course) (bundle.getSerializable("course"));
            addCourse(course);
        }
    }*/

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            Log.v("Tag", "onWindowFocusChanged:" + "true");
        } else {
            Log.v("Tag", "onWindowFocusChanged:" + "false");
        }
        if (isFirst) {
            isFirst = false;
            gridWidth = MonLayout.getWidth();
            gridHeight = MonLayout.getHeight() / 16;
        }
        super.onWindowFocusChanged(hasFocus);
    }


    private void registerCourseAddButton() {
        Button[] course_num = new Button[16];
        course_num[0] = (Button)findViewById(R.id._1);
        course_num[1] = (Button)findViewById(R.id._2);
        course_num[2] = (Button)findViewById(R.id._3);
        course_num[3] = (Button)findViewById(R.id._4);
        course_num[4] = (Button)findViewById(R.id._5);
        course_num[5] = (Button)findViewById(R.id._6);
        course_num[6] = (Button)findViewById(R.id._7);
        course_num[7] = (Button)findViewById(R.id._8);
        course_num[8] = (Button)findViewById(R.id._9);
        course_num[9] = (Button)findViewById(R.id._10);
        course_num[10] = (Button)findViewById(R.id._11);
        course_num[11] = (Button)findViewById(R.id._12);
        course_num[12] = (Button)findViewById(R.id._13);
        course_num[13] = (Button)findViewById(R.id._14);
        course_num[14] = (Button)findViewById(R.id._15);
        course_num[15] = (Button)findViewById(R.id._16);
        View.OnClickListener add = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button temp = (Button)v;
                String num = temp.getText().toString();
                int start = Integer.parseInt(num);
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("start", start);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        };
        for (int i = 0; i < 16; i++) {
            course_num[i].setOnClickListener(add);
        }
    }

    static public TimerService timerService;
    static public ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            timerService = ((TimerService.MyBinder)(service)).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            timerService = null;
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
 }
