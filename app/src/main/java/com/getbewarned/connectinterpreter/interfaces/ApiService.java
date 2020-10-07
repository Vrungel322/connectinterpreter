package com.getbewarned.connectinterpreter.interfaces;

import com.getbewarned.connectinterpreter.models.ApiResponseBase;
import com.getbewarned.connectinterpreter.models.AppVersionResponse;
import com.getbewarned.connectinterpreter.models.AvailabilityResponse;
import com.getbewarned.connectinterpreter.models.CountriesResponse;
import com.getbewarned.connectinterpreter.models.GroupSessionResponse;
import com.getbewarned.connectinterpreter.models.LiqPayResponse;
import com.getbewarned.connectinterpreter.models.LoginResponse;
import com.getbewarned.connectinterpreter.models.MessagesResponse;
import com.getbewarned.connectinterpreter.models.NameResponse;
import com.getbewarned.connectinterpreter.models.NewMessageResponse;
import com.getbewarned.connectinterpreter.models.NewRequestResponse;
import com.getbewarned.connectinterpreter.models.NewsResponse;
import com.getbewarned.connectinterpreter.models.ReasonsResponse;
import com.getbewarned.connectinterpreter.models.RequestsResponse;
import com.getbewarned.connectinterpreter.models.TariffsResponse;
import com.getbewarned.connectinterpreter.models.TokenResponse;
import com.getbewarned.connectinterpreter.models.UtogResponse;

import kotlin.PublishedApi;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
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

    @PUT("api/v3/profile")
    @FormUrlEncoded
    Call<NameResponse> updateProfile(@Header("X-Interpreter-Client-Token") String authorization,
                                     @Field("name") String name,
                                     @Query("lang") String language);

    @POST("api/v3/compensation")
    @FormUrlEncoded
    Call<ApiResponseBase> updateCompensationInfo(@Header("X-Interpreter-Client-Token") String authorization,
                                              @Field("first_name") String firstName,
                                              @Field("last_name") String lastName,
                                              @Field("patronymic") String patronymic,
                                              @Field("birthday") String birthday,
                                              @Field("passport_number_series") String passport,
                                              @Field("itn") String itn,
                                              @Field("inila") String inila,
                                              @Field("city") String city,
                                              @Field("street") String street,
                                              @Field("number") String number,
                                              @Field("apartment") String apartment,
                                              @Field("postcode") String postcode);

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
    Call<TariffsResponse> getTariffs(@Header("X-Interpreter-Client-Token") String authorization,
                                     @Query("lang") String language);

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

    @GET("/api/client/reasons")
    Call<ReasonsResponse> getReasons(@Query("lang") String language);

    @POST("/api/client/call_review")
    @FormUrlEncoded
    Call<ApiResponseBase> leaveReview(@Header("X-Interpreter-Client-Token") String authorization,
                                      @Field("session_id") String sessionId,
                                      @Field("rate") int rate,
                                      @Field("review") String review);


    @POST("/api/client/login_help")
    @FormUrlEncoded
    Call<ApiResponseBase> loginHelp(@Field("phone") String phone);

    @GET("/api/client/requests")
    Call<RequestsResponse> getRequests(@Header("X-Interpreter-Client-Token") String authorization);

    @POST("/api/client/requests")
    @Multipart
    Call<NewRequestResponse> newRequest(@Header("X-Interpreter-Client-Token") String authorization,
                                        @Part MultipartBody.Part media);

    @GET("/api/client/requests/{request}")
    Call<MessagesResponse> getRequestMessages(@Header("X-Interpreter-Client-Token") String authorization,
                                              @Path("request") long requestId);

    @POST("/api/client/requests/{request}")
    @Multipart
    Call<NewMessageResponse> newRequestMessage(@Header("X-Interpreter-Client-Token") String authorization,
                                               @Path("request") long requestId,
                                               @Part MultipartBody.Part media,
                                               @Part("type") RequestBody type);

    @GET("/api/client/group/{session}")
    Call<GroupSessionResponse> connectToGroupSession(@Header("X-Interpreter-Client-Token") String authorization,
                                                     @Path("session") String sessionId);

    @POST("/api/client/group/{session}/askers")
    Call<ApiResponseBase> askQuestion(@Header("X-Interpreter-Client-Token") String authorization,
                                      @Path("session") String sessionId);

    @DELETE("/api/client/group/{session}/askers")
    Call<ApiResponseBase> stopAsking(@Header("X-Interpreter-Client-Token") String authorization,
                                     @Path("session") String sessionId);

    @GET("/api/client/news")
    Call<NewsResponse> getNews(@Header("X-Interpreter-Client-Token") String authorization,
                               @Query("lang") String language);

}
