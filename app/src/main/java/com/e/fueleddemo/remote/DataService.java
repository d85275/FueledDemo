package com.e.fueleddemo.remote;

import com.e.fueleddemo.restaurant_model.SearchResult;

import io.reactivex.Single;

import retrofit2.http.GET;


public interface DataService {

    String categoryId = "4d4b7105d754a06374d81259";
    String clientId = "O0YFMUA13FQJ4OWDSJTGJ1P12BH5SXOCDCFYTLQ2BRVO0ITE";
    String clientSecret = "TDKD442AKEODDDGPBM0VLGZ1QOIDLVWUU5VD1E5BIVULVZ54";
    String v = "20191214";
    String ll = "51.5174800,-0.0827000";
    String radius = "1500";
    String limit = "50";

    String request = "search?categoryId=" + categoryId + "&client_id=" + clientId +
            "&client_secret=" + clientSecret + "&v=" + v + "&ll=" + ll + "&radius=" + radius + "&limit=" + limit;

    @GET(request)
    Single<SearchResult> getVenues();

}
