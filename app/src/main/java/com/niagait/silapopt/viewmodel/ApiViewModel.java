package com.niagait.silapopt.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.niagait.silapopt.model.LoginModel;
import com.niagait.silapopt.model.SlideModel;
import com.niagait.silapopt.model.repository.ApiRepository;

import java.util.HashMap;

public class ApiViewModel extends ViewModel {
    private ApiRepository apiRepository;
    private LoginModel loginModel;

    public ApiViewModel(ApiRepository apiRepository) {
        this.apiRepository = apiRepository;
    }

    public void setLoginModel(LoginModel loginModel) {
        this.loginModel = loginModel;
    }

    public LiveData<LoginModel> login() {
        return apiRepository.login(loginModel);
    }

    public LiveData<HashMap<String, Object>> slide() {
        return apiRepository.slide();
    }
}
