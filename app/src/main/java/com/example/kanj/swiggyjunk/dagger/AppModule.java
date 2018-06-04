package com.example.kanj.swiggyjunk.dagger;

import com.example.kanj.swiggyjunk.api.MyJsonService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {
    @Provides
    @Singleton
    public MyJsonService provideMyJsonService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.myjson.com/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(MyJsonService.class);
    }
}
