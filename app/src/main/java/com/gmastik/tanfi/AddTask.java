package com.gmastik.tanfi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
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

import java.util.HashMap;
import java.util.Objects;

public class AddTask extends AppCompatActivity {

    // variables
    TextView taskType;
    ImageView cancel,addTime;
    TextInputEditText title,desc;
    Button save;
    String getTaskType,getTitle,getDesc;
    FirebaseDatabase root;
    DatabaseReference userRef;
    Dialog popup;
    String getGetTaskType;
    long id = 0;

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
        popup = new Dialog(this);

        cancel.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),TaskDashboard.class);
            startActivity(intent);

            overridePendingTransition(R.anim.slide_down_in,R.anim.slide_down_out);
        });

        // here is popup with radio :v
        taskType.setOnClickListener(v -> {
            popup.setContentView(R.layout.popup_get_task_type);
            popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            Button daily,project;
            daily = popup.findViewById(R.id.daily);
            project = popup.findViewById(R.id.project);
            popup.show();

            daily.setOnClickListener(view -> {
                taskType.setText("Harian");
                getGetTaskType = "Harian";
                popup.dismiss();
            } );
            project.setOnClickListener(view -> {
                taskType.setText("Proyek");
                getGetTaskType = "Proyek";
                popup.dismiss();
            });
        });

        save.setOnClickListener(v -> saveUserTask());
    }

    public void toTaskDashboard(){
        startActivity(new Intent(AddTask.this,TaskDashboard.class));
        finish();
    }

    public void saveUserTask(){
        // get string data
        getTaskType = getGetTaskType; //taskType.getEditableText().toString();
        getTitle = title.getEditableText().toString();
        getDesc = desc.getEditableText().toString();
        /* here is get time */

        // check field
        if(getTitle.isEmpty()){
            title.setError("Isi judul tugas anda");
            title.requestFocus();
        } else if(getDesc.isEmpty()){
            desc.setError("Isi deskripsi tugas anda untuk mempermudah anda");
            desc.requestFocus();
        } else { uploadData(getTaskType,getTitle,getDesc); }
    }

    public void uploadData(String taskType,String title,String desc){
        // get DB reference
        String userID;
        FirebaseAuth auth;
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
                id = (snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        HashMap<String,String> userData = new HashMap<>();
        userData.put("task type",taskType);
        userData.put("title",title);
        userData.put("desc",desc);

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