package com.e.fueleddemo.dependency_injection.modules;

import com.e.fueleddemo.remote.DataService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = MyViewModelModule.class)
public abstract class NetworkModule {

    private static final String BASE_URL = "https://api.foursquare.com/v2/venues/";

    @Provides
    @Singleton
    static Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    static DataService provideDataService(Retrofit retrofit){
        return retrofit.create(DataService.class);
    }
}
