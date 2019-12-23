package com.e.fueleddemo.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface HideRestaurantDao {
    @Query("SELECT * FROM hiderestaurant")
    Single<List<HideRestaurant>> getAll();

    @Insert
    void insertAll(HideRestaurant... hideRestaurants);

    @Delete
    void delete(HideRestaurant hideRestaurant);
}
