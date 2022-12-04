package com.gmastik.tanfi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gmastik.tanfi.adapters.OnboardingAdapter;
import com.google.firebase.auth.FirebaseAuth;

public class OnboardingScreen extends AppCompatActivity {

    // variables
    ViewPager viewPager;
    LinearLayout dotsLayout;
    OnboardingAdapter onboardingAdapter;
    TextView[] dots;
    Button getStarted;
    FirebaseAuth auth;
    int currentPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_screen);

        auth = FirebaseAuth.getInstance();

        viewPager = findViewById(R.id.view_pager);
        dotsLayout = findViewById(R.id.dots);
        getStarted = findViewById(R.id.get_started);

        // set adapter
        onboardingAdapter = new OnboardingAdapter(this);
        viewPager.setAdapter(onboardingAdapter);

        addDots(0);
        viewPager.addOnPageChangeListener(changeListener);

        getStarted.setOnClickListener(v -> {
            // check auth then to dashboard
            if(auth.getCurrentUser() != null){
                toMainApp();
            } else {
                startActivity(new Intent(OnboardingScreen.this,Login.class));
            }
        });
    }

    public void next(View view){
        viewPager.setCurrentItem(currentPos+1);
    }

    private void addDots(int position){
        dots = new TextView[2];
        dotsLayout.removeAllViews();
        for(int i = 0; i < dots.length; i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextColor(getResources().getColor(R.color.mainBg));
            dots[i].setTextSize(50);
            dotsLayout.addView(dots[i]);
        }

        if(dots.length > 0){
            dots[position].setTextColor(getResources().getColor(R.color.purplePrimary));
        }
    }

    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);
            currentPos = position;

            if(position == 0){
                getStarted.setVisibility(View.INVISIBLE);
            } else if(position == 1) {
                getStarted.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public void toMainApp(){
        startActivity(new Intent(OnboardingScreen.this,TaskDashboard.class));
        finish();
    }
}