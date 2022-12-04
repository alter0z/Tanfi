package com.gmastik.tanfi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;
import java.util.TimeZone;

public class AddTask extends AppCompatActivity {

    // variables
    TextView taskType,beginDate,beginTime,endTime;
    ImageView cancel,addTime,beginTimeSet,endTimeSet;
    TextInputEditText title,desc,tags;
    Button save,timeSave,saveBeginTime,saveEndTime;
    String getTaskType,getTitle,getDesc,getTime,getDay,getMonth,getYear,getTimeBegin,getTimeEnd,getHourOfBegin,getHourOfEnd,getMinuteOfBegin,getMinuteOfEnd;
    FirebaseDatabase root;
    DatabaseReference userRef;
    FirebaseAuth auth;
    Dialog popup,calendarShow,beginTimePicker,endTimePicker;
    String getGetTaskType = "Harian";
    String getTimeRole = "Tidak ada";
    String getTaskTags = "Tags belum dimasukkan";
    String userID;
    long id = 0;
    RadioGroup rbGroup, radioGroup;
    RadioButton rbDaily,rbProject,rbNothing,rbWithDaily,rbWithWeekly;
    CalendarView cv;
    TimePicker getBeginTimePicker,getEndTimePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        // find id
        taskType = findViewById(R.id.task_type);
        cancel = findViewById(R.id.x);
        addTime = findViewById(R.id.add_time);
        title = findViewById(R.id.daily_task_title);
        desc = findViewById(R.id.desc);
        save = findViewById(R.id.save_daily_task);
        beginDate = findViewById(R.id.times);
        tags = findViewById(R.id.task_tag);
        beginTimeSet = findViewById(R.id.begin_time_set);
        endTimeSet = findViewById(R.id.end_time_set);
        beginTime = findViewById(R.id.time_begin);
        endTime = findViewById(R.id.time_end);
        popup = new Dialog(this);
        calendarShow = new Dialog(this);
        beginTimePicker = new Dialog(this);
        endTimePicker = new Dialog(this);

        // popup get task type
        popup.setContentView(R.layout.popup_get_task_type);
        popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        rbGroup = popup.findViewById(R.id.rbGroup);
        rbDaily = popup.findViewById(R.id.daily);
        rbProject = popup.findViewById(R.id.project);

        // popup calendar show
        calendarShow.setContentView(R.layout.calendar_show);
        calendarShow.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cv = calendarShow.findViewById(R.id.cv);
        timeSave = calendarShow.findViewById(R.id.save_time);
        radioGroup = calendarShow.findViewById(R.id.radio_group);
        rbNothing = calendarShow.findViewById(R.id.nothing);
        rbWithDaily = calendarShow.findViewById(R.id.with_daily);
        rbWithWeekly = calendarShow.findViewById(R.id.with_weekly);

        // popup begin time picker
        beginTimePicker.setContentView(R.layout.begin_time_picker);
        beginTimePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getBeginTimePicker = beginTimePicker.findViewById(R.id.get_begin_time);
        saveBeginTime = beginTimePicker.findViewById(R.id.save_begin_time);

        // popup end time picker
        endTimePicker.setContentView(R.layout.end_time_picker);
        endTimePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getEndTimePicker = endTimePicker.findViewById(R.id.get_end_time);
        saveEndTime = endTimePicker.findViewById(R.id.save_end_time);


        // get DB reference
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        assert user != null;
        userID = user.getUid();
        root = FirebaseDatabase.getInstance();
        userRef = root.getReference("users").child(userID).child("tasks");

        // data count method (lol) :v
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists())
                    id = (snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        // canceling action
        cancel.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),TaskDashboard.class);
            startActivity(intent);

            overridePendingTransition(R.anim.slide_down_in,R.anim.slide_down_out);
        });

        // adding time
        addTime.setOnClickListener(v -> {
            calendarShow.show();
            cv.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
                String date = dayOfMonth + "." + month + "." + year;
                String getDays = String.valueOf(dayOfMonth);
                String getMonths = String.valueOf(month);
                String getYears = String.valueOf(year);

                // get role time with checked radio button
                radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                    if (rbNothing.isChecked())
                    {
                        getTimeRole = "Tidak ada";
                    }
                    if (rbWithDaily.isChecked())
                    {
                        getTimeRole = "Harian";
                    }
                    if(rbWithWeekly.isChecked()){
                        getTimeRole = "Mingguan";
                    }
                });

                // on save clicked
                timeSave.setOnClickListener(views -> {
                    getTime = date;
                    getDay = getDays;
                    getMonth = getMonths;
                    getYear = getYears;
                    beginDate.setText(date);
                    calendarShow.dismiss();
                });
            });
        });

        // here is popup with radio :v
        taskType.setOnClickListener(view -> {
            popup.show();
            rbGroup.setOnCheckedChangeListener((group, checkedId) -> {
                if (rbDaily.isChecked())
                {
                    taskType.setText("Harian");
                    getGetTaskType = "Harian";
                }
                if (rbProject.isChecked())
                {
                    taskType.setText("Proyek");
                    getGetTaskType = "Proyek";
                }
                popup.dismiss();
            });
        });

        // picking begin time
        beginTimeSet.setOnClickListener(v -> {
            beginTimePicker.show();
            getBeginTimePicker.setIs24HourView(true);
            getBeginTimePicker.setOnTimeChangedListener((view, hourOfDay, minute) -> {
                String setBeginTime = hourOfDay+"."+minute;
                String hourOfBegin = String.valueOf(hourOfDay);
                String minuteOfBegin = String.valueOf(minute);

                saveBeginTime.setOnClickListener(views -> {
                    getTimeBegin = setBeginTime;
                    getHourOfBegin = hourOfBegin;
                    getMinuteOfBegin = minuteOfBegin;
                    beginTime.setText(getTimeBegin);
                    beginTimePicker.dismiss();
                });
            });
        });

        // picking end time
        endTimeSet.setOnClickListener(v -> {
            endTimePicker.show();
            getEndTimePicker.setIs24HourView(true);
            getEndTimePicker.setOnTimeChangedListener((view, hourOfDay, minute) -> {
                String setEndTime = hourOfDay+"."+minute;
                String hourOfEnd = String.valueOf(hourOfDay);
                String minuteOfEnd = String.valueOf(minute);

                saveEndTime.setOnClickListener(views-> {
                    getTimeEnd = setEndTime;
                    getHourOfEnd = hourOfEnd;
                    getMinuteOfEnd = minuteOfEnd;
                    endTime.setText(getTimeEnd);
                    endTimePicker.dismiss();
                });
            });
        });

        // save the values
        save.setOnClickListener(view -> saveUserTask());
    }

    public void toTaskDashboard(){
        startActivity(new Intent(AddTask.this,TaskDashboard.class));
        finish();
    }

    public void saveUserTask(){
        // get string data
        getTaskType = getGetTaskType;
        getTitle = title.getEditableText().toString();
        getDesc = desc.getEditableText().toString();
        getTaskTags = tags.getEditableText().toString();

        String timeLine = (getTimeBegin+" - "+getTimeEnd);

        // check field
        if(getTitle.isEmpty()){
            title.setError("Isi judul tugas anda");
            title.requestFocus();
        } else if(getDesc.isEmpty()){
            desc.setError("Isi deskripsi tugas anda untuk mempermudah anda");
            desc.requestFocus();
        } else { uploadData(getTaskType,getTitle,getDesc,getTime,getDay,getMonth,getYear,getTimeRole,getTaskTags,timeLine,getTimeBegin,getTimeEnd,getHourOfBegin,getMinuteOfBegin,getHourOfEnd,getMinuteOfEnd); }
    }

    public void uploadData(String taskType,String title,String desc,String date,String day,String month,String year,String timeRole,String tags,String timeLine,String beginTime,String endTime,String hourBeginTime,String minuteBeginTime,String hourEndTime,String minuteEndTime){

        HashMap<String,String> userData = new HashMap<>();
        userData.put("task_type",taskType);
        userData.put("title",title);
        userData.put("desc",desc);
        userData.put("date",date);
        userData.put("day",day);
        userData.put("month",month);
        userData.put("year",year);
        userData.put("time_role",timeRole);
        userData.put("tags",tags);
        userData.put("time_line",timeLine);
        userData.put("begin_time",beginTime);
        userData.put("end_time",endTime);
        userData.put("hour_begin_time",hourBeginTime);
        userData.put("minute_begin_time",minuteBeginTime);
        userData.put("hour_end_time",hourEndTime);
        userData.put("minute_end_time",minuteEndTime);

        userRef.child(String.valueOf(id+1)).setValue(userData).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(AddTask.this,"Tugas berhasil disimpan",Toast.LENGTH_SHORT).show();
                toTaskDashboard();
            } else {
                Toast.makeText(AddTask.this,"Error: "+ Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(AddTask.this,"Error: "+e.getMessage(),Toast.LENGTH_SHORT).show());
    }
}