package com.quantumtech.vido;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.quantumtech.vido.activites.IntroActivity;

public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
            setTheme(R.style.NightTheme);
        } else {
            setTheme(R.style.BaseTheme);
        }
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(SplashScreen.this, IntroActivity.class);
        startActivity(intent);
        finish();
    }
}
