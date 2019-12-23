package com.e.fueleddemo.dependency_injection.modules;


import android.content.Context;

import androidx.room.Room;

import com.e.fueleddemo.database.AppDatabase;
import com.e.fueleddemo.database.HideRestaurantDao;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {

    private Context ctx;
    private final String dbName = "hide_restaurants";


    @Inject
    public DatabaseModule(Context ctx) {
        this.ctx = ctx;
    }

    @Singleton
    @Provides
    AppDatabase provideDatabase() {
        return Room.databaseBuilder(
                ctx,
                AppDatabase.class,
                dbName
        ).build();
    }

    @Singleton
    @Provides
    HideRestaurantDao provideHideRestaurantDao(AppDatabase db) {
        return db.hideRestaurantDao();
    }
}
