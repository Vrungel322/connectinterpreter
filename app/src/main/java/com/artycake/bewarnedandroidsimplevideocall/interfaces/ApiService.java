package com.artycake.bewarnedandroidsimplevideocall.interfaces;

import com.artycake.bewarnedandroidsimplevideocall.models.OpenTokTokenResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by artycake on 10/11/17.
 */

public interface ApiService {
    @GET("/video-token-second")
    Call<OpenTokTokenResponse> getVideoTokenSecond(@Header("Authorization") String authorization);
}
