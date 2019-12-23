package com.e.fueleddemo.dependency_injection.component;

import com.e.fueleddemo.MainActivity;
import com.e.fueleddemo.dependency_injection.modules.DatabaseModule;
import com.e.fueleddemo.dependency_injection.modules.ContextModule;
import com.e.fueleddemo.dependency_injection.modules.NetworkModule;

import javax.inject.Singleton;

@Singleton
@dagger.Component(modules = {NetworkModule.class, ContextModule.class, DatabaseModule.class})
public interface AppComponent {

    void inject(MainActivity mainActivity);
}
