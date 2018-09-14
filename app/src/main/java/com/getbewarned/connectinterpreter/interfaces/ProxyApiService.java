package com.getbewarned.connectinterpreter.interfaces;


import com.getbewarned.connectinterpreter.models.ProxyResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ProxyApiService {
    @GET("/proxy")
    Call<ProxyResponse> getProxy(@Query("allowsHttps") boolean withHttps);
}
