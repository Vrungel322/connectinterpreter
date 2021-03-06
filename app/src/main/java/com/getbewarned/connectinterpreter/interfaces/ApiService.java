package com.getbewarned.connectinterpreter.interfaces;

import com.getbewarned.connectinterpreter.models.ApiResponseBase;
import com.getbewarned.connectinterpreter.models.AppVersionResponse;
import com.getbewarned.connectinterpreter.models.AvailabilityResponse;
import com.getbewarned.connectinterpreter.models.CountriesResponse;
import com.getbewarned.connectinterpreter.models.CreateYandexPaymentResponse;
import com.getbewarned.connectinterpreter.models.GroupSessionResponse;
import com.getbewarned.connectinterpreter.models.LiqPayResponse;
import com.getbewarned.connectinterpreter.models.LoginResponse;
import com.getbewarned.connectinterpreter.models.MessagesResponse;
import com.getbewarned.connectinterpreter.models.NameResponse;
import com.getbewarned.connectinterpreter.models.NewMessageResponse;
import com.getbewarned.connectinterpreter.models.NewRequestResponse;
import com.getbewarned.connectinterpreter.models.NewsResponse;
import com.getbewarned.connectinterpreter.models.ProfileResponse;
import com.getbewarned.connectinterpreter.models.ReasonsResponse;
import com.getbewarned.connectinterpreter.models.RequestsResponse;
import com.getbewarned.connectinterpreter.models.TariffsResponse;
import com.getbewarned.connectinterpreter.models.TariffsResponseV2;
import com.getbewarned.connectinterpreter.models.TokenResponse;
import com.getbewarned.connectinterpreter.models.UtogResponse;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
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
    Call<ApiResponseBase> getCode(@HeaderMap Map<String, String> headers,
                                  @Field("phone") String phone);

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
    Call<NameResponse> updateName(@HeaderMap Map<String, String> headers,
                                  @Field("name") String name,
                                  @Query("lang") String language);

    @PUT("api/v3/profile")
    @FormUrlEncoded
    Call<ProfileResponse> updateProfile(@HeaderMap Map<String, String> headers,
                                        @Field("first_name") String firstName,
                                        @Field("last_name") String lastName,
                                        @Field("patronymic") String patronymic,
                                        @Field("country") String country,
                                        @Field("city") String city,
                                        @Query("lang") String language);

    @GET("api/v3/profile")
    Call<ProfileResponse> getProfile(@HeaderMap Map<String, String> headers,
                                     @Query("lang") String language);

    @POST("api/v3/compensation")
    @FormUrlEncoded
    Call<ApiResponseBase> updateCompensationInfo(@HeaderMap Map<String, String> headers,
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
    Call<AvailabilityResponse> updateAvailability(@HeaderMap Map<String, String> headers,
                                                  @Query("lang") String language);

    @POST("/api/client/logout")
    Call<ApiResponseBase> logout(@HeaderMap Map<String, String> headers,
                                 @Query("lang") String language);

    @POST("/api/client/make_call")
    @FormUrlEncoded
    Call<TokenResponse> makeCall(@HeaderMap Map<String, String> headers,
                                 @Field("reason") String reason,
                                 @Query("lang") String language);

    @POST("/api/client/send_message")
    @FormUrlEncoded
    Call<ApiResponseBase> sendMessage(@HeaderMap Map<String, String> headers,
                                      @Field("session_id") String sessionId,
                                      @Field("message") String message,
                                      @Query("lang") String language);


    @GET("/api/client/unlim_list")
    Call<TariffsResponse> getTariffs(@HeaderMap Map<String, String> headers,
                                     @Query("lang") String language);

    @GET("/api/v3/tariffs")
    Call<TariffsResponseV2> getTariffsV2(@HeaderMap Map<String, String> headers,
                                         @Query("lang") String language);

    @POST("/api/v3/payment/create")
    @FormUrlEncoded
    Call<CreateYandexPaymentResponse> createYandexPayment(@HeaderMap Map<String, String> headers,
                                                          @Field("token") String token,
                                                          @Field("tariff_id") String id,
                                                          @Query("lang") String language);

    @POST("/api/v3/payment/approve")
    @FormUrlEncoded
    Call<ApiResponseBase> approveYandexPayment(@HeaderMap Map<String, String> headers,
                                               @Field("payment_id") String id,
                                               @Query("lang") String language);

    @POST("/api/client/buy_unlim")
    @FormUrlEncoded
    Call<LiqPayResponse> buyUnlim(@HeaderMap Map<String, String> headers,
                                  @Field("tariff") String tariff,
                                  @Query("lang") String language);

    @POST("/api/client/notification_token")
    @FormUrlEncoded
    Call<ApiResponseBase> sendNotificationToken(@HeaderMap Map<String, String> headers,
                                                @Field("token") String token);

    @POST("/api/client/send_message")
    @FormUrlEncoded
    Call<ApiResponseBase> sendMessageToCall(@HeaderMap Map<String, String> headers,
                                            @Field("session_id") String sessionId,
                                            @Field("message") String message);

    @GET("/api/client/version")
    Call<AppVersionResponse> getAppVersion(@Query("platform") String platform, @Query("lang") String language);


    @POST("/api/client/utog_info")
    @FormUrlEncoded
    Call<UtogResponse> sendUtogInfo(@HeaderMap Map<String, String> headers,
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
    Call<ApiResponseBase> leaveReview(@HeaderMap Map<String, String> headers,
                                      @Field("session_id") String sessionId,
                                      @Field("rate") int rate,
                                      @Field("review") String review);


    @POST("/api/client/login_help")
    @FormUrlEncoded
    Call<ApiResponseBase> loginHelp(@Field("phone") String phone);

    @GET("/api/client/requests")
    Call<RequestsResponse> getRequests(@HeaderMap Map<String, String> headers);

    @POST("/api/client/requests")
    @Multipart
    Call<NewRequestResponse> newRequest(@HeaderMap Map<String, String> headers,
                                        @Part MultipartBody.Part media);

    @GET("/api/client/requests/{request}")
    Call<MessagesResponse> getRequestMessages(@HeaderMap Map<String, String> headers,
                                              @Path("request") long requestId);

    @POST("/api/client/requests/{request}")
    @Multipart
    Call<NewMessageResponse> newRequestMessage(@HeaderMap Map<String, String> headers,
                                               @Path("request") long requestId,
                                               @Part MultipartBody.Part media,
                                               @Part("type") RequestBody type);

    @GET("/api/client/group/{session}")
    Call<GroupSessionResponse> connectToGroupSession(@HeaderMap Map<String, String> headers,
                                                     @Path("session") String sessionId);

    @POST("/api/client/group/{session}/askers")
    Call<ApiResponseBase> askQuestion(@HeaderMap Map<String, String> headers,
                                      @Path("session") String sessionId);

    @DELETE("/api/client/group/{session}/askers")
    Call<ApiResponseBase> stopAsking(@HeaderMap Map<String, String> headers,
                                     @Path("session") String sessionId);

    @GET("/api/client/news")
    Call<NewsResponse> getNews(@HeaderMap Map<String, String> headers,
                               @Query("lang") String language);

}
