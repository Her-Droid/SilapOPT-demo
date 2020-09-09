package com.niagait.silapopt.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.niagait.silapopt.R;

import java.util.Calendar;

public class DataActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private Context context;
    private EditText etDate;
    private BottomNavigationView navigation;
    private Spinner mySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
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
                break;
            case R.id.nav_maps:
                intent = new Intent(context, GlobalActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                break;
            case R.id.nav_weather:
                intent = new Intent(context, IklimActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
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
        context = DataActivity.this;

        View layoutData = findViewById(R.id.layoutData);
        navigation = layoutData.findViewById(R.id.navigation);
        mySpinner = layoutData.findViewById(R.id.spinner);
        etDate = layoutData.findViewById(R.id.etdate);
    }

    private void setView() {
        navigation.setOnNavigationItemSelectedListener(this);
        navigation.getMenu().findItem(R.id.nav_dataopt).setChecked(true);
        //Defining Spinner for Komoditas
        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(DataActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.comodity));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);

        //Defining DatePciker for Tanggal Pengamatan
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get (Calendar.YEAR);
        final int month = calendar.get (Calendar.MONTH);
        final int day = calendar.get (Calendar.DAY_OF_MONTH);

        etDate.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    DataActivity.this, (view1, year1, month1, day1) -> {
                month1 = month1 + 1;
                String date = day1 +"/"+ month1 +"/"+ year1;
                etDate.setText(date);
            },year,month,day);
            datePickerDialog.show();
        });
    }

}