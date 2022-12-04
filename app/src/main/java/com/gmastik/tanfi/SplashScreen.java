package com.gmastik.tanfi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.gmastik.tanfi.adapters.OnboardingAdapter;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {

    // variables
    private static int SPLASH_SCREEN = 500;
    FirebaseAuth auth;
    SharedPreferences onBoardingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        auth = FirebaseAuth.getInstance();

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        new Handler().postDelayed((Runnable) () -> {

            // is first time checked
            onBoardingScreen = getSharedPreferences("onBoardingScreen",MODE_PRIVATE);
            boolean isFirstTime = onBoardingScreen.getBoolean("firstTime",true);

            if(isFirstTime){
                SharedPreferences.Editor editor = onBoardingScreen.edit();
                editor.putBoolean("firstTime",false);
                editor.apply();
                startActivity(new Intent(SplashScreen.this,OnboardingScreen.class));
                finish();
            } else {
                // check auth then to dashboard
                if(auth.getCurrentUser() != null){
                    toMainApp();
                } else {
                    startActivity(new Intent(SplashScreen.this,Login.class));
                    finish();
                }
            }
        },SPLASH_SCREEN);
    }

    public void toMainApp(){
        startActivity(new Intent(SplashScreen.this,TaskDashboard.class));
        finish();
    }
}