package com.niagait.silapopt.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.niagait.silapopt.R;

import static com.niagait.silapopt.view.config.Config.SESSION;
import static com.niagait.silapopt.view.config.Config.SHAREDPREFERENCES;

public class LandingActivity extends AppCompatActivity {
    private Context context;

    private boolean session = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        inisialisasiView();
        setView();
    }

    private void inisialisasiView() {
        context = LandingActivity.this;
        SharedPreferences sharedPreferences = getSharedPreferences(SHAREDPREFERENCES, Context.MODE_PRIVATE);
        session = sharedPreferences.getBoolean(SESSION, false);
        Log.v("jajal", session+" a");
    }

    private void setView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LandingActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);

    }
}