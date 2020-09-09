package com.niagait.silapopt.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.niagait.silapopt.R;

public class IklimActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private Context context;
    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iklim);
        inisialisasiView();
        setView();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.home:
                intent = new Intent(context, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                break;
            case R.id.nav_dataopt:
                intent = new Intent(context, DataActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                break;
            case R.id.nav_maps:
                intent = new Intent(context, GlobalActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                break;
            case R.id.nav_weather:
                break;
            case R.id.nav_workplan:
                intent = new Intent(context, RencanaActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                break;
        }
        return false;
    }

    private void inisialisasiView() {
        context = IklimActivity.this;

        View layoutIklim = findViewById(R.id.layoutIklim);
        navigation = layoutIklim.findViewById(R.id.navigation);
    }

    private void setView() {
        navigation.setOnNavigationItemSelectedListener(this);
        navigation.getMenu().findItem(R.id.nav_weather).setChecked(true);
    }
}