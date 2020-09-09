package com.niagait.silapopt.model.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.niagait.silapopt.model.SlideModel;
import com.niagait.silapopt.view.callback.LoginRetrofitCallBack;
import com.niagait.silapopt.model.LoginModel;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiRepository {
    private Call<ResponseBody> responseBodyCall;
    private String responses;
    private JSONObject jsonObject;

    private LoginRetrofitCallBack getApi() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://silapopt.niagait.com/RestApi/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit.create(LoginRetrofitCallBack.class);
    }

    public LiveData<LoginModel> login(LoginModel loginModel1) {
        final MutableLiveData<LoginModel> memberModelMutableLiveData = new MutableLiveData<>();
        final LoginModel loginModel = new LoginModel();

        responseBodyCall = getApi().login(
                loginModel1.getEmail(),
                loginModel1.getPassword(),
                loginModel1.getToken_firebase(),
                loginModel1.getFbclid()
        );
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {

                if (response.body() != null) {
                    try {
                        responses = response.body().string();
                        jsonObject = new JSONObject(responses);

                        loginModel.setStatus(String.valueOf(jsonObject.getBoolean("status")));
                        if (jsonObject.getBoolean("status")) {
                            jsonObject = new JSONObject(jsonObject.getString("data"));
                            loginModel.setNama(jsonObject.getString("nama"));
                            loginModel.setNip(jsonObject.getString("nip"));
                            loginModel.setJabatan(jsonObject.getString("jabatan"));
                        } else {
                            loginModel.setResult(jsonObject.getString("msg"));
                        }
                    } catch (IOException | JSONException e) {
                        loginModel.setStatus("3");
                        e.printStackTrace();
                    }
                } else {
                    loginModel.setStatus("3");
                }
                memberModelMutableLiveData.setValue(loginModel);
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    loginModel.setStatus("1");
                } else if (t instanceof IOException) {
                    loginModel.setStatus("2");
                } else {
                    loginModel.setStatus("3");
                }

                memberModelMutableLiveData.setValue(loginModel);
            }
        });
        return memberModelMutableLiveData;
    }

    public LiveData<HashMap<String, Object>> slide() {
        final MutableLiveData<HashMap<String, Object>> hashMapMutableLiveData = new MutableLiveData<>();
        HashMap<String, Object> hashMap = new HashMap<>();

        responseBodyCall = getApi().slide();
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {

                if (response.body() != null) {
                    try {
                        responses = response.body().string();
                        jsonObject = new JSONObject(responses);

                        hashMap.put("code",String.valueOf(jsonObject.getBoolean("status")));
                        if (jsonObject.getBoolean("status")) {
                            JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                            ArrayList<SlideModel> slideModels = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObject = jsonArray.getJSONObject(i);
                                SlideModel slideModel = new SlideModel();
                                slideModel.setImage(jsonObject.getString("file"));
                                slideModel.setJudul(jsonObject.getString("judul"));
                                slideModel.setCatatan(jsonObject.getString("catatan"));
                                slideModels.add(slideModel);
                            }
                            hashMap.put("list", slideModels);
                        }
                    } catch (IOException | JSONException e) {
                        hashMap.put("code","3");
                        e.printStackTrace();
                    }
                } else {
                    hashMap.put("code","3");
                }
                hashMapMutableLiveData.setValue(hashMap);
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    hashMap.put("code","1");
                } else if (t instanceof IOException) {
                    hashMap.put("code","2");
                } else {
                    hashMap.put("code","3");
                }

                hashMapMutableLiveData.setValue(hashMap);
            }
        });
        return hashMapMutableLiveData;
    }

}
