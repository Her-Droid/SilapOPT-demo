package com.niagait.silapopt.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.niagait.silapopt.R;
import com.niagait.silapopt.model.LoginModel;
import com.niagait.silapopt.viewmodel.ApiViewModel;
import com.niagait.silapopt.viewmodel.viewmodelfactory.ViewModelFactory;

import java.util.Objects;

import static com.niagait.silapopt.view.config.Config.NAMA;
import static com.niagait.silapopt.view.config.Config.SESSION;
import static com.niagait.silapopt.view.config.Config.SHAREDPREFERENCES;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private ApiViewModel apiViewModel;

    private TextInputEditText edtUsername,
            edtPassword;
    private Button btnMasuk;
    private LinearLayout layoutMasuk;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inisialisasiView();
        setView();
    }

    @Override
    public void onClick(View v) {
        if (v == btnMasuk){
            login();
        }
    }

    private void inisialisasiView() {
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnMasuk = findViewById(R.id.btnMasuk);
        layoutMasuk = findViewById(R.id.layoutMasuk);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setView() {
        context = LoginActivity.this;
        apiViewModel = new ViewModelProvider(getViewModelStore(), new ViewModelFactory()).get(ApiViewModel.class);
        btnMasuk.setOnClickListener(this);
    }

    private void login(){
        if (Objects.requireNonNull(edtPassword.getText()).toString().trim().isEmpty()){
            edtUsername.setError(getResources().getString(R.string.usernamekosong));
            edtUsername.requestFocus();
        } else if (Objects.requireNonNull(edtPassword.getText()).toString().trim().isEmpty()){
            edtPassword.setError(getResources().getString(R.string.passwordkosong));
            edtPassword.requestFocus();
        } else{
            layoutMasuk.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            //input
            LoginModel loginModel1 = new LoginModel();
            loginModel1.setEmail(Objects.requireNonNull(edtUsername.getText()).toString().trim());
            loginModel1.setPassword(Objects.requireNonNull(edtPassword.getText()).toString().trim());
            loginModel1.setToken_firebase("dpQhpF-CI4s:APA91bGo_W8fVT_yob9tV0FiVobujs-DxgjtR9o-9sK80J8nt6edQZ_t2DhfIUYc-p5fPjHLOnWdinIoR_M8tTnd3K_LUMEJXtosovPpyBBOLd9eiTtyWc5J7POUYvBcUWoqJtkO7Oai");
            loginModel1.setFbclid("IwAR0J0ATNQSlFyOYRHguDjOLyZi_F2BpEg4uZvlxCGHBkSH90eTnd8-gM9yU");

            //proses
            apiViewModel.setLoginModel(loginModel1);
            apiViewModel.login().observe(this, loginModel -> {
                switch (loginModel.getStatus()) {
                    case "true":
                        //pindah activity
                        SharedPreferences sharedPreferences = getSharedPreferences(SHAREDPREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(SESSION, true);
                        editor.putString(NAMA, loginModel.getNama());
                        editor.apply();
                        startActivity( new Intent(context, MainActivity.class));
                        finish();
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
                        Toast.makeText(context, loginModel.getResult(), Toast.LENGTH_LONG).show();
                        break;
                }
                layoutMasuk.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            });
        }
    }
}