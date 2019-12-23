package com.e.fueleddemo.database;


import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(entities = {HideRestaurant.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract HideRestaurantDao hideRestaurantDao();

}
