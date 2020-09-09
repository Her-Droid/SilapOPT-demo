package com.niagait.silapopt.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.niagait.silapopt.R;

import java.util.Calendar;

public class RencanaActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private Context context;

    private BottomNavigationView navigation;
    private Button btnRencanaKerja;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rencana);
        inisialisasiView();
        setView();
    }

    @Override
    public void onClick(View v) {
        if (v == btnRencanaKerja) {
            addTambahKerja();
        }
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
                intent = new Intent(context, IklimActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                break;
            case R.id.nav_workplan:
                break;
        }
        return false;
    }

    private void inisialisasiView() {
        context = RencanaActivity.this;

        View layoutRencana = findViewById(R.id.layoutRencana);
        navigation = layoutRencana.findViewById(R.id.navigation);
        btnRencanaKerja = layoutRencana.findViewById(R.id.btnRencanaKerja);
    }

    private void setView() {
        navigation.setOnNavigationItemSelectedListener(this);
        navigation.getMenu().findItem(R.id.nav_workplan).setChecked(true);

        btnRencanaKerja.setOnClickListener(this);
    }

    @SuppressLint("InflateParams")
    private void addTambahKerja() {
        AlertDialog.Builder alertbuilder = new AlertDialog.Builder(context);
        alertbuilder.setCancelable(true);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        alertbuilder.setView(inflater.inflate(R.layout.form_rencanakerja, null));
        AlertDialog alert = alertbuilder.create();
        alert.show();

        EditText edtTanggal = alert.findViewById(R.id.edtTanggal);
        EditText edtWaktu = alert.findViewById(R.id.edtWaktu);
        EditText edtKegiatan = alert.findViewById(R.id.edtKegiatan);
        EditText edtUlasan = alert.findViewById(R.id.edtUlasan);

        if (edtTanggal != null && edtWaktu != null && edtKegiatan != null && edtUlasan != null) {
            //Defining DatePciker for Tanggal Pengamatan
            Calendar calendar = Calendar.getInstance();
            final int year = calendar.get(Calendar.YEAR);
            final int month = calendar.get(Calendar.MONTH);
            final int day = calendar.get(Calendar.DAY_OF_MONTH);

            edtTanggal.setFocusable(false);
            edtTanggal.setOnClickListener(view -> {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        context, (view1, year1, month1, day1) -> {
                    month1 = month1 + 1;
                    String date = day1 + "/" + month1 + "/" + year1;
                    edtTanggal.setText(date);
                }, year, month, day);
                datePickerDialog.show();
            });

            edtWaktu.setFocusable(false);
            edtWaktu.setOnClickListener(v -> {
                new TimePickerDialog(this, (view, hourOfDay, minute) -> {
                    String time = hourOfDay + ":" + minute;
                    edtWaktu.setText(time);
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(this)).show();
            });
        }
    }
}