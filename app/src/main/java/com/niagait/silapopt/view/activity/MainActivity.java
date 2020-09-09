package com.niagait.silapopt.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.niagait.silapopt.R;
import com.niagait.silapopt.model.SlideModel;
import com.niagait.silapopt.view.adapter.BannerViewPagerAdapter;
import com.niagait.silapopt.viewmodel.ApiViewModel;
import com.niagait.silapopt.viewmodel.viewmodelfactory.ViewModelFactory;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import static com.niagait.silapopt.view.config.Config.NAMA;
import static com.niagait.silapopt.view.config.Config.SESSION;
import static com.niagait.silapopt.view.config.Config.SHAREDPREFERENCES;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {
    private Context context;
    private SharedPreferences sharedPreferences;
    private ApiViewModel apiViewModel;
    private Timer timer;

    private TextView lblNama,
            lblKeluar;
    private ViewPager viewPager;
    private SmartTabLayout layoutDots;
    private BottomNavigationView navigation;

    private String nama;
    private ArrayList<SlideModel> slideModels;

    private static final String TAG = "MyTag";
    private TextView mOutputText;

    private static final String FCM_CHANNEL_ID = "FCM_CHANNEL_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inisialisasiView();
        setView();

        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }
        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }
        // [END handle_data_extras]
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        FirebaseMessaging.getInstance().subscribeToTopic(FCM_CHANNEL_ID)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.msg_subscribe_failed);
                        }
                        Log.d(TAG, msg);
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    @Override
    public void onClick(View v) {
        if (v == lblKeluar) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(SESSION, false);
            editor.putString(NAMA, null);
            editor.apply();

            startActivity(new Intent(context, LoginActivity.class));
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.home:
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
        context = MainActivity.this;
        apiViewModel = new ViewModelProvider(getViewModelStore(), new ViewModelFactory()).get(ApiViewModel.class);

        sharedPreferences = getSharedPreferences(SHAREDPREFERENCES, Context.MODE_PRIVATE);
        nama = sharedPreferences.getString(NAMA, "");

        View layoutMain = findViewById(R.id.layoutMain);
        navigation = layoutMain.findViewById(R.id.navigation);

        lblNama = findViewById(R.id.lblNama);
        lblKeluar = findViewById(R.id.lblKeluar);
        viewPager = findViewById(R.id.viewPager);
        layoutDots = findViewById(R.id.layoutDots);
    }

    @SuppressWarnings("unchecked")
    private void setView() {
        lblNama.setText(nama);
        lblKeluar.setOnClickListener(this);
        navigation.setOnNavigationItemSelectedListener(this);
        navigation.getMenu().findItem(R.id.home).setChecked(true);

        apiViewModel.slide().observe(this, hashMap -> {
            switch (Objects.requireNonNull(hashMap.get("code")).toString()) {
                case "true":
                    slideModels = (ArrayList<SlideModel>) hashMap.get("list");
                    BannerViewPagerAdapter bannerViewPagerAdapter = new BannerViewPagerAdapter(context, slideModels);
                    viewPager.setAdapter(bannerViewPagerAdapter);

                    layoutDots.setViewPager(viewPager);
                    if (slideModels.size() > 1) {
                        timer = new Timer();
                        timer.scheduleAtFixedRate(new MyTimerTask(), 2000, 4000);
                    }

                    break;
                case "1":
                    Toast.makeText(context, "Timeout connection", Toast.LENGTH_LONG).show();
                    break;
                case "2":
                    Toast.makeText(context, "No connection", Toast.LENGTH_LONG).show();
                    break;
                case "3":
                    Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        });
    }

    public class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            MainActivity.this.runOnUiThread(() -> {
                if (viewPager.getCurrentItem() + 1 == slideModels.size()) {
                    viewPager.setCurrentItem(0);
                } else {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                }
            });
        }
    }
}