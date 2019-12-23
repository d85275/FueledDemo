package com.e.fueleddemo.dependency_injection.modules;


import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.e.fueleddemo.viewmodel.MyViewModel;
import com.e.fueleddemo.dependency_injection.ViewModelKey;
import com.e.fueleddemo.viewmodel.MyViewModelFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class MyViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MyViewModel.class)
    abstract ViewModel bindViewModel(MyViewModel myViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindFactory(MyViewModelFactory factory);
}
