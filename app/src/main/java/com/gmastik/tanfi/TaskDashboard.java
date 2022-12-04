package com.gmastik.tanfi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.gmastik.tanfi.fragments.FinanceFragment;
import com.gmastik.tanfi.fragments.TaskFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class TaskDashboard extends AppCompatActivity implements TaskFragment.TaskFragmentListener {

    // variables
    FirebaseAuth auth;
    ImageView addTask;
    BottomNavigationView bottomNavigationView;
    RelativeLayout layout,logout;
    LinearLayout navDrawer;
//    CategoryTaskAdapter categoryTaskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_dashboard);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        addTask = findViewById(R.id.add_task);
        layout = findViewById(R.id.layout);
        navDrawer = findViewById(R.id.nav_drawer);
        logout = findViewById(R.id.logout);

        // set layout position
        navDrawer.setTranslationX(1100f);
        navDrawer.setAlpha(1f);

        auth = FirebaseAuth.getInstance();

        // bottom nav on action
        bottomNavigationView.getMenu().getItem(0).setEnabled(false);
        bottomNavigationView.getMenu().getItem(1).setChecked(true);
        bottomNavigationView.setOnItemSelectedListener(navigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TaskFragment()).commit();

//        // show nav
//        showNav.setOnClickListener(v -> {
//            navDrawer.animate().translationX(600f).alpha(1f).setDuration(300).start();
//            layout.animate().translationX(-500f).alpha(1f).setDuration(300).start();
//            showNav.setVisibility(View.INVISIBLE);
//            closeNav.setVisibility(View.VISIBLE);
//        });
//
//        // close nav
//        closeNav.setOnClickListener(v -> {
//            navDrawer.animate().translationX(1100f).alpha(1f).setDuration(300).start();
//            layout.animate().translationX(0f).alpha(1f).setDuration(300).start();
//            showNav.setVisibility(View.VISIBLE);
//            closeNav.setVisibility(View.INVISIBLE);
//        });

        // logout onClick
        logout.setOnClickListener(v -> signOut());

        addTask.setOnClickListener(v -> addTask());
    }

    @Override
    protected void onStart() {
        super.onStart();
        // if user isn't present (null) direct to login
        FirebaseUser user = auth.getCurrentUser();
        if(user == null){
            startActivity(new Intent(TaskDashboard.this,Login.class));
            finish();
        }
    }

    public void addTask(){
        startActivity(new Intent(TaskDashboard.this,AddTask.class));
        overridePendingTransition(R.anim.slide_up_in,R.anim.slide_up_out);
    }

    public void signOut(){
        auth.signOut();
        startActivity(new Intent(TaskDashboard.this,Login.class));
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = item -> {
        Fragment selectedFragment = null;

            switch (item.getItemId()){
                case R.id.task_nav:
                    selectedFragment = new TaskFragment(); break;
                case R.id.finance_nav:
                    selectedFragment = new FinanceFragment(); break;
            }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
        return true;
    };

    // get status from fragment
    @Override
    public void oButtonClick(String status) {
        if(status.equals("closed")){
            navDrawer.animate().translationX(1100f).alpha(1f).setDuration(300).start();
            layout.animate().translationX(0f).alpha(1f).setDuration(300).start();
        } else if(status.equals("opened")){
            navDrawer.animate().translationX(600f).alpha(1f).setDuration(300).start();
            layout.animate().translationX(-500f).alpha(1f).setDuration(300).start();
        }
    }
}