package com.e.fueleddemo.database;

import androidx.room.TypeConverter;

import com.e.fueleddemo.restaurant_model.Venue;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class DataConverter {

    @TypeConverter
    public String fromVenue(Venue venue) {
        if (venue == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<Venue>() {
        }.getType();
        return gson.toJson(venue, type);
    }

    @TypeConverter
    public Venue toVenue(String sVenue) {
        if (sVenue == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<Venue>() {
        }.getType();
        return gson.fromJson(sVenue, type);
    }
}
