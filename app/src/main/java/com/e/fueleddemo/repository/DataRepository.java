package com.e.fueleddemo.repository;

import com.e.fueleddemo.database.AppDatabase;
import com.e.fueleddemo.database.HideRestaurant;
import com.e.fueleddemo.remote.DataService;
import com.e.fueleddemo.restaurant_model.SearchResult;
import com.e.fueleddemo.restaurant_model.Venue;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class DataRepository {

    @Inject
    AppDatabase appDatabase;
    private DataService service;


    @Inject
    public DataRepository(DataService service) {
        this.service = service;
    }

    // remote data
    public Single<SearchResult> venueSingle() {
        return service.getVenues();
    }


    // local data
    public Single<List<HideRestaurant>> getHide() {
        return appDatabase.hideRestaurantDao().getAll();
    }

    public void addHide(HideRestaurant hideRestaurant) {
        appDatabase.hideRestaurantDao().insertAll(hideRestaurant);
    }

    public void deleteHide(HideRestaurant hideRestaurant) {
        appDatabase.hideRestaurantDao().delete(hideRestaurant);
    }

}
