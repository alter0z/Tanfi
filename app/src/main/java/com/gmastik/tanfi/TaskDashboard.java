package com.gmastik.tanfi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gmastik.tanfi.adapters.CategoryTaskAdapter;
import com.gmastik.tanfi.configs.RvCategoryTaskConfig;
import com.gmastik.tanfi.helperClass.FirebaseDatabaseHelperClass;
import com.gmastik.tanfi.models.CategoryTaskModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TaskDashboard extends AppCompatActivity {

    // variables
    TextView date;
    ImageView addTask;
    FirebaseAuth auth;
    BottomNavigationView bottomNavigationView;
    RecyclerView categoryRecycle;
//    CategoryTaskAdapter categoryTaskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_dashboard);

        // date init
        Calendar calendar = Calendar.getInstance();
        String nowDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        // set the values into array
//        ArrayList<CategoryTaskModel> items = new ArrayList<>();
//        items.add(new CategoryTaskModel("semua"));

        date = findViewById(R.id.date);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        categoryRecycle = findViewById(R.id.category_task);
        addTask = findViewById(R.id.add_task);

        date.setText(nowDate);

        auth = FirebaseAuth.getInstance();

        // set recycle adapter
//        categoryTaskAdapter = new CategoryTaskAdapter(items);
//        categoryRecycle.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
//        categoryRecycle.setAdapter(categoryTaskAdapter);

        // bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(0).setEnabled(false);

        addTask.setOnClickListener(v -> addTask());

        // read data
        new FirebaseDatabaseHelperClass().readItem(new FirebaseDatabaseHelperClass.DataStatues() {
            @Override
            public void DataIsLoaded(List<CategoryTaskModel> tags, List<String> key) {
                new RvCategoryTaskConfig().setConfig(categoryRecycle,TaskDashboard.this,tags,key);
            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // if user isn't present (null) direct to login
        FirebaseUser user = auth.getCurrentUser();
        if(user == null){
            startActivity(new Intent(TaskDashboard.this,Login.class));
        }
    }

    public void addTask(){
        startActivity(new Intent(TaskDashboard.this,AddTask.class));
        overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
    }
}