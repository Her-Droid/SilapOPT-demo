package com.niagait.silapopt.viewmodel.viewmodelfactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.niagait.silapopt.model.repository.ApiRepository;
import com.niagait.silapopt.viewmodel.ApiViewModel;


public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ApiViewModel.class)) {
            ApiRepository apiRepository = new ApiRepository();
            return (T) new ApiViewModel(apiRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
