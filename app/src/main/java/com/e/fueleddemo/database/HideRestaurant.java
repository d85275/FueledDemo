package com.e.fueleddemo.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.e.fueleddemo.restaurant_model.Venue;

@Entity
public class HideRestaurant {

    // auto generate the primary key
    @PrimaryKey(autoGenerate = true)
    public int id;

    @TypeConverters(DataConverter.class)
    @ColumnInfo(name = "venue")
    public Venue venue;
}
