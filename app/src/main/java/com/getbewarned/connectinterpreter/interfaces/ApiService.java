package com.getbewarned.connectinterpreter.interfaces;

import com.getbewarned.connectinterpreter.models.ApiResponseBase;
import com.getbewarned.connectinterpreter.models.AppVersionResponse;
import com.getbewarned.connectinterpreter.models.CountriesResponse;
import com.getbewarned.connectinterpreter.models.LiqPayResponse;
import com.getbewarned.connectinterpreter.models.LoginResponse;
import com.getbewarned.connectinterpreter.models.AvailabilityResponse;
import com.getbewarned.connectinterpreter.models.NameResponse;
import com.getbewarned.connectinterpreter.models.TariffsResponse;
import com.getbewarned.connectinterpreter.models.TokenResponse;
import com.getbewarned.connectinterpreter.models.UtogResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by artycake on 10/11/17.
 */

public interface ApiService {

    @POST("api/client/get_code")
    @FormUrlEncoded
    Call<ApiResponseBase> getCode(@Field("phone") String phone);

    @POST("api/client/login")
    @FormUrlEncoded
    Call<LoginResponse> login(@Field("phone") String phone,
                              @Field("code") String code,
                              @Field("device_id") String deviceId,
                              @Field("device_name") String deviceName,
                              @Query("lang") String language
    );

    @POST("api/client/update_name")
    @FormUrlEncoded
    Call<NameResponse> updateName(@Header("X-Interpreter-Client-Token") String authorization,
                                  @Field("name") String name,
                                  @Query("lang") String language);

    @GET("api/client/availability")
    Call<AvailabilityResponse> updateAvailability(@Header("X-Interpreter-Client-Token") String authorization,
                                                  @Query("lang") String language);

    @POST("/api/client/logout")
    Call<ApiResponseBase> logout(@Header("X-Interpreter-Client-Token") String authorization,
                                 @Query("lang") String language);

    @POST("/api/client/make_call")
    @FormUrlEncoded
    Call<TokenResponse> makeCall(@Header("X-Interpreter-Client-Token") String authorization,
                                 @Field("reason") String reason,
                                 @Query("lang") String language);

    @POST("/api/client/send_message")
    @FormUrlEncoded
    Call<ApiResponseBase> sendMessage(@Header("X-Interpreter-Client-Token") String authorization,
                                      @Field("session_id") String sessionId,
                                      @Field("message") String message,
                                      @Query("lang") String language);


    @GET("/api/client/unlim_list")
    Call<TariffsResponse> getTariffs(@Query("lang") String language);

    @POST("/api/client/buy_unlim")
    @FormUrlEncoded
    Call<LiqPayResponse> buyUnlim(@Header("X-Interpreter-Client-Token") String authorization,
                                  @Field("tariff") String tariff,
                                  @Query("lang") String language);

    @POST("/api/client/notification_token")
    @FormUrlEncoded
    Call<ApiResponseBase> sendNotificationToken(@Header("X-Interpreter-Client-Token") String authorization,
                                                @Field("token") String token);

    @POST("/api/client/send_message")
    @FormUrlEncoded
    Call<ApiResponseBase> sendMessageToCall(@Header("X-Interpreter-Client-Token") String authorization,
                                            @Field("session_id") String sessionId,
                                            @Field("message") String message);

    @GET("/api/client/version")
    Call<AppVersionResponse> getAppVersion(@Query("platform") String platform, @Query("lang") String language);


    @POST("/api/client/utog_info")
    @FormUrlEncoded
    Call<UtogResponse> sendUtogInfo(@Header("X-Interpreter-Client-Token") String authorization,
                                    @Field("first_name") String firstName,
                                    @Field("last_name") String lastName,
                                    @Field("patronymic") String patronymic,
                                    @Field("member_id") String memberId,
                                    @Query("lang") String language);

    @GET("/api/client/countries")
    Call<CountriesResponse> getCountries(@Query("lang") String language);

    @POST("/api/client/call_review")
    @FormUrlEncoded
    Call<ApiResponseBase> leaveReview(@Header("X-Interpreter-Client-Token") String authorization,
                                      @Field("session_id") String sessionId,
                                      @Field("rate") int rate,
                                      @Field("review") String review);


    @POST("/api/client/login_help")
    @FormUrlEncoded
    Call<ApiResponseBase> loginHelp(@Field("phone") String phone);

}
