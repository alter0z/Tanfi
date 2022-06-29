package com.gmastik.tanfi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {

    // variables
    private static int SPLASH_SCREEN = 5000;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        auth = FirebaseAuth.getInstance();

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // check auth then to dashboard
                if(auth.getCurrentUser() != null){
                    toMainApp();
                } else {
                    startActivity(new Intent(SplashScreen.this,Login.class));
                }
            }
        },SPLASH_SCREEN);
    }

    public void toMainApp(){
        startActivity(new Intent(SplashScreen.this,TaskDashboard.class));
        finish();
    }
}