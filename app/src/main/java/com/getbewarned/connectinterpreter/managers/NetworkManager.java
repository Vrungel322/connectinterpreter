package com.getbewarned.connectinterpreter.managers;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.getbewarned.connectinterpreter.interfaces.ApiService;
import com.getbewarned.connectinterpreter.interfaces.TokenReceived;
import com.getbewarned.connectinterpreter.models.OpenTokTokenResponse;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by artycake on 10/11/17.
 */

public class NetworkManager {
    private static final String BASE_URL = "https://getbw.me";

    private static final String KEY = "client-app-android";
    private static final String VALUE = "secret-for-client-ec-v1";
    private static final String BASIC = "Basic ";
    public static final String UTF_8 = "UTF-8";

    private ApiService api;

    public NetworkManager(Context context) {

        Retrofit builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()))
                .build();

        api = builder.create(ApiService.class);
    }

    private String getEncodedAuthKey() {
        String encodedKey = BASIC;
        byte[] byteArray;
        try {
            String key = KEY + ":" + VALUE;
            byteArray = key.getBytes(UTF_8);
            encodedKey += Base64.encodeToString(byteArray,
                    Base64.DEFAULT);
            encodedKey = encodedKey.trim();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Log.d("Encoded key = ", "" + encodedKey);
        return encodedKey;
    }

    public void getToken(final TokenReceived tokenReceived) {
        Call<OpenTokTokenResponse> call = api.getVideoTokenSecond(getEncodedAuthKey());
        call.enqueue(new Callback<OpenTokTokenResponse>() {
            @Override
            public void onResponse(Call<OpenTokTokenResponse> call, Response<OpenTokTokenResponse> response) {
                tokenReceived.onTokenReceived(response.body());
            }

            @Override
            public void onFailure(Call<OpenTokTokenResponse> call, Throwable t) {
                tokenReceived.onErrorReceived(new Error(t));
            }
        });
    }
}
