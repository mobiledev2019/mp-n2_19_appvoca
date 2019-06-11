package com.luyendd.learntoeic.activity;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.luyendd.learntoeic.ConnectDataBase;
import com.luyendd.learntoeic.R;
import com.luyendd.learntoeic.adapter.AdapterMainStatistic;
import com.luyendd.learntoeic.adapter.AdapterTopic;
import com.luyendd.learntoeic.obj.Topic;
import com.luyendd.learntoeic.utils.AlarmUtil;
import com.luyendd.learntoeic.utils.Const;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    GridView gridView, gridViewFavourite;
    public static ConnectDataBase cdb;
    List<Topic> topicList;
    AdapterMainStatistic adapterTopic;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();

        dl = (DrawerLayout) findViewById(R.id.activity_main);
        t = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);
        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = (NavigationView) findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.menu_remider_favourite:
                        if (AlarmUtil.initAlarm(MainActivity.this, true)) {
                            showDialogRemiderFavourite();
                        }
                        return true;

                    case R.id.menu_favourite:
                        Intent i = new Intent(MainActivity.this, VocaDetailsActivity.class);
                        i.putExtra(Const.TOPIC_FAVOURITE, true);
                        i.putExtra(Const.TOPIC_NAME, "Favourite");
                        startActivity(i);
                        return true;

                    case R.id.menu_reminder_voca:
                        if (AlarmUtil.initAlarm(MainActivity.this, false)) {
                            showDialogReminderVoca();
                        }
                        return true;

                    case R.id.menu_topic:
                        startActivity(new Intent(MainActivity.this, TopicActivity.class));
                        return true;

                    default:
                        return true;
                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        cdb = new ConnectDataBase(this);
        try {
            cdb.createDataBase();
            topicList = cdb.getListTopicStatistical();
        } catch (IOException e) {
            e.printStackTrace();
        }

        adapterTopic = new AdapterMainStatistic(this, topicList);
        gridView = findViewById(R.id.gridview);
        gridView.setAdapter(adapterTopic);
        adapterTopic.notifyDataSetChanged();

        gridViewFavourite = findViewById(R.id.gridview_faourite);
        try {
            AdapterTopic adapterTopicFavourite = new AdapterTopic(this, cdb.getListTopicFavourite());
            gridViewFavourite.setAdapter(adapterTopicFavourite);
            adapterTopicFavourite.notifyDataSetChanged();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void settingTime() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, final int selectedHour, final int selectedMinute) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,
                                selectedHour + ":" + selectedMinute, Toast.LENGTH_SHORT).show();
                        editor.putInt(Const.ALARM_LIST_FAVORITE_HOUR, selectedHour);
                        editor.putInt(Const.ALARM_LIST_FAVORITE_MINUTE, selectedMinute);
                        editor.commit();
                    }
                });
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    private void showDialogRemiderFavourite() {

        int hour, minute;
        boolean isReminder;
        hour = sharedPreferences.getInt(Const.ALARM_LIST_FAVORITE_HOUR, Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
        minute = sharedPreferences.getInt(Const.ALARM_LIST_FAVORITE_MINUTE, Calendar.getInstance().get(Calendar.MINUTE));
        isReminder = sharedPreferences.getBoolean(Const.IS_REMINDER_FAVORITE, false);
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_remider);

        final Switch swReminder = dialog.findViewById(R.id.switchReminder);
        final TimePicker timePicker = dialog.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        if (Build.VERSION.SDK_INT > 22) {
            timePicker.setHour(hour);
            timePicker.setMinute(minute);
        } else {
            timePicker.setCurrentHour(hour);
            timePicker.setCurrentMinute(minute);
        }

        Button btSave = dialog.findViewById(R.id.btnSaveTime);
        Button btClose = dialog.findViewById(R.id.btnClose);

        swReminder.setChecked(isReminder);
        swReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean(Const.IS_REMINDER_FAVORITE, isChecked).commit();
                Log.d(TAG, "[switch status] : " + isChecked);
                if (isChecked) {
                    if (!AlarmUtil.initAlarm(MainActivity.this, true)) {
                        Toast.makeText(MainActivity.this, "Voca favourite is null", Toast.LENGTH_SHORT).show();
                        swReminder.setChecked(false);
                    }
                }
            }
        });

        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPreferences.getBoolean(Const.IS_REMINDER_FAVORITE, false)) {
                    AlarmUtil.turnOnAlarm(MainActivity.this, true);
                } else {
                    AlarmUtil.turnOffAlarm();
                }
                dialog.dismiss();
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = Build.VERSION.SDK_INT > 22 ? timePicker.getHour() : timePicker.getCurrentHour();
                int minute = Build.VERSION.SDK_INT > 22 ? timePicker.getMinute() : timePicker.getCurrentMinute();
                editor.putInt(Const.ALARM_LIST_FAVORITE_MINUTE, minute);
                editor.putInt(Const.ALARM_LIST_FAVORITE_HOUR, hour);
                editor.commit();
                Log.d(TAG, "[Button Save] " + hour + " : " + minute);
                if (sharedPreferences.getBoolean(Const.IS_REMINDER_FAVORITE, false)) {
                    AlarmUtil.turnOnAlarm(MainActivity.this, true);
                } else {
                    AlarmUtil.turnOffAlarm();
                }

                dialog.dismiss();
            }
        });

        dialog.create();
        dialog.show();


    }

    private void showDialogReminderVoca() {

        boolean isReminder;
        int minute = sharedPreferences.getInt(Const.ALARM_VOCA_MINUTE_REPEAT, 10);
        isReminder = sharedPreferences.getBoolean(Const.IS_REMINDER_VOCA, false);
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_repeat_voca);

        final Switch swReminder = dialog.findViewById(R.id.switchReminder);

        Button btSave = dialog.findViewById(R.id.btnSaveTime);
        Button btClose = dialog.findViewById(R.id.btnClose);

        final EditText edMinute = dialog.findViewById(R.id.edit_text_minute_repeat);
        edMinute.setText(minute + "");

        swReminder.setChecked(isReminder);
        swReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    if (!AlarmUtil.initAlarm(MainActivity.this, false)) {
                        Toast.makeText(MainActivity.this, "Voca favourite is null", Toast.LENGTH_SHORT).show();
                        swReminder.setChecked(false);
                    }
                }
                editor.putBoolean(Const.IS_REMINDER_VOCA, swReminder.isChecked()).commit();
                Log.d(TAG, "[switch status] : " + swReminder.isChecked());

            }
        });

        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPreferences.getBoolean(Const.IS_REMINDER_VOCA, false)) {
                    AlarmUtil.turnOnAlarm(MainActivity.this, false);
                } else {
                    AlarmUtil.turnOffAlarm();
                }
                dialog.dismiss();
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edMinute.getText().length() > 0) {
                    editor.putInt(Const.ALARM_VOCA_MINUTE_REPEAT, Integer.parseInt(edMinute.getText().toString()));
                    editor.commit();
                    Log.d(TAG, "[Button Save] Minute : " + edMinute.getText());
                    if (sharedPreferences.getBoolean(Const.IS_REMINDER_VOCA, false)) {
                        AlarmUtil.turnOnAlarm(MainActivity.this, false);
                    } else {
                        AlarmUtil.turnOffAlarm();
                    }

                    dialog.dismiss();
                } else {
                    edMinute.setError("Input is empty");
                }

            }
        });

        dialog.create();
        dialog.show();
    }

}
