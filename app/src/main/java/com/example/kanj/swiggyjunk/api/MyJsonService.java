package com.example.kanj.swiggyjunk.api;

import com.example.kanj.swiggyjunk.api.model.MyJsonResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface MyJsonService {
    @GET("/bins/3b0u2")
    Observable<MyJsonResponse> getMyJson();
}
