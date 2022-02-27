package com.quantumtech.vido.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.quantumtech.vido.R;
import com.quantumtech.vido.adapter.IntroViewPagerAdapter;
import com.quantumtech.vido.model.IntroScreen;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {

    IntroViewPagerAdapter adapter;
    ViewPager viewPager;
    TabLayout tabLayout;
    TextView nextButton;
    TextView skipButton;
    Button getStartedButton;
    Animation showGetStartedAnim;
    Animation hideGetStartedAnim;
    private List<IntroScreen> mList;
    private int position;
    private int lastPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(restorePref()){
            startMainActivity();
        }

        setContentView(R.layout.activity_intro);

        tabLayout = findViewById(R.id.tabLayout);
        nextButton = findViewById(R.id.next_btn);
        skipButton = findViewById(R.id.skip_btn);
        getStartedButton = findViewById(R.id.get_started_btn);
        showGetStartedAnim = AnimationUtils.loadAnimation(this,R.anim.show_get_started_btn);
        hideGetStartedAnim = AnimationUtils.loadAnimation(this,R.anim.hide_get_started_btn);
        getStartedButton.setVisibility(View.INVISIBLE);

        mList = new ArrayList<>();
        mList.add(new IntroScreen("Fist Title","Take me dipe in presens of my sprite Take me dipe in presens of my sprite", R.drawable.s1));
        mList.add(new IntroScreen("Second Title","Take me dipe in presens of my sprite", R.drawable.s2));
        mList.add(new IntroScreen("Tried Title","Take me dipe in presens of my sprite", R.drawable.s3));

        viewPager = findViewById(R.id.screen_pager);
        adapter = new IntroViewPagerAdapter(this,mList);
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if(tab.getPosition()==mList.size()-1){
                    loadLastScreen();
                    hideGetStartedAnim.reset();
                }
                else if(tab.getPosition()<mList.size()){
                    loadOtherScreen();
                    showGetStartedAnim.reset();
                }

                lastPosition = position;
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNextButtonClicked();
            }
        });
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMainActivity();
            }
        });
        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMainActivity();
            }
        });
    }

    private boolean restorePref() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("vido_preferences", MODE_PRIVATE);
        return preferences.getBoolean("isIntroFinished", false);
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("vido_preferences",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isIntroFinished", true);
        editor.apply();
        finish();

    }

    private void onNextButtonClicked() {
        position = viewPager.getCurrentItem();

        if(position<mList.size()){
            position++;
            viewPager.setCurrentItem(position);
        }

        if(position == mList.size()-1){
            loadLastScreen();
        }

        lastPosition = position;
    }

    private void loadLastScreen() {
        nextButton.setAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
        skipButton.setAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
        tabLayout.setAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
        nextButton.setVisibility(View.INVISIBLE);
        skipButton.setVisibility(View.INVISIBLE);
        getStartedButton.setVisibility(View.VISIBLE);
        tabLayout.setVisibility(View.INVISIBLE);
        nextButton.setEnabled(false);
        getStartedButton.setAnimation(showGetStartedAnim);
        showGetStartedAnim.start();
    }

    private void loadOtherScreen(){
        if(!nextButton.isEnabled()){
            nextButton.setAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
            skipButton.setAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
            tabLayout.setAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
            nextButton.setVisibility(View.VISIBLE);
            skipButton.setVisibility(View.VISIBLE);
            getStartedButton.setVisibility(View.INVISIBLE);
            tabLayout.setVisibility(View.VISIBLE);
            nextButton.setEnabled(true);
            getStartedButton.setAnimation(hideGetStartedAnim);
            hideGetStartedAnim.start();
        }
    }
}
