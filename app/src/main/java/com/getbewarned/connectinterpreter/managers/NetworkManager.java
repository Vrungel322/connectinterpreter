package com.getbewarned.connectinterpreter.managers;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.interfaces.ApiService;
import com.getbewarned.connectinterpreter.interfaces.AppVersionReceived;
import com.getbewarned.connectinterpreter.interfaces.AvailabilityReceived;
import com.getbewarned.connectinterpreter.interfaces.BaseRequestCompleted;
import com.getbewarned.connectinterpreter.interfaces.CodeReceived;
import com.getbewarned.connectinterpreter.interfaces.CountriesReceived;
import com.getbewarned.connectinterpreter.interfaces.GroupSessionReceived;
import com.getbewarned.connectinterpreter.interfaces.HelpRequested;
import com.getbewarned.connectinterpreter.interfaces.LiqPayDataReceived;
import com.getbewarned.connectinterpreter.interfaces.LoginComplete;
import com.getbewarned.connectinterpreter.interfaces.MessageSent;
import com.getbewarned.connectinterpreter.interfaces.NameChanged;
import com.getbewarned.connectinterpreter.interfaces.NewRequestCreated;
import com.getbewarned.connectinterpreter.interfaces.NewRequestMessageCreated;
import com.getbewarned.connectinterpreter.interfaces.NewsReceived;
import com.getbewarned.connectinterpreter.interfaces.ProfileReceived;
import com.getbewarned.connectinterpreter.interfaces.ReasonsReceived;
import com.getbewarned.connectinterpreter.interfaces.RequestMessagesReceived;
import com.getbewarned.connectinterpreter.interfaces.RequestsReceived;
import com.getbewarned.connectinterpreter.interfaces.TariffsReceived;
import com.getbewarned.connectinterpreter.interfaces.TariffsReceivedV2;
import com.getbewarned.connectinterpreter.interfaces.TokenReceived;
import com.getbewarned.connectinterpreter.interfaces.UnauthRequestHandler;
import com.getbewarned.connectinterpreter.interfaces.UtogResponseReceived;
import com.getbewarned.connectinterpreter.interfaces.YandexKassaPaymentReceived;
import com.getbewarned.connectinterpreter.interfaces.YandexPaymentApprove;
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
import com.getbewarned.connectinterpreter.models.Request;
import com.getbewarned.connectinterpreter.models.RequestsResponse;
import com.getbewarned.connectinterpreter.models.TariffsResponse;
import com.getbewarned.connectinterpreter.models.TariffsResponseV2;
import com.getbewarned.connectinterpreter.models.TokenResponse;
import com.getbewarned.connectinterpreter.models.UtogResponse;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by artycake on 10/11/17.
 */

public class NetworkManager {
    private static final String BASE_URL = "https://interpreter.getbw.me";

    private final ApiService api;
    private String authToken;
    private final Retrofit retrofit;
    private final Context context;


    private UnauthRequestHandler unauthRequestHandler;

    public NetworkManager(Context context) {
        this.context = context;

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(loggingInterceptor);
//        client.proxySelector(ProxySelector.getDefault());
//        client.proxyAuthenticator(new okhttp3.Authenticator() {
//            @Nullable
//            @Override
//            public okhttp3.Request authenticate(Route route, okhttp3.Response response) throws IOException {
//                InetSocketAddress socketAddress = (InetSocketAddress) route.proxy().address();
//                if (socketAddress.getAddress().toString().equals("/193.22.99.11")) {
//                    return response.request().newBuilder()
//                            .header("Proxy-Connection", "Keep-Alive")
//                            .header("Proxy-Authorization", Credentials.basic("ACtanN", "eMf7aK"))
//                            .build();
//                }
//                if (socketAddress.getAddress().toString().equals("/176.107.186.200")) {
//                    return response.request().newBuilder()
//                            .header("Proxy-Connection", "Keep-Alive")
//                            .header("Proxy-Authorization", Credentials.basic("GsH2zL", "DS6BZJ"))
//                            .build();
//                }
//                return response.request();
//            }
//        });
        client.retryOnConnectionFailure(true);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()))
                .build();

        api = retrofit.create(ApiService.class);
    }

    public void setUnauthRequestHandler(UnauthRequestHandler unauthRequestHandler) {
        this.unauthRequestHandler = unauthRequestHandler;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void getCode(String phone, final CodeReceived codeReceived) {
        Call<ApiResponseBase> call = api.getCode(phone);
        call.enqueue(new Callback<ApiResponseBase>() {
            @Override
            public void onResponse(Call<ApiResponseBase> call, Response<ApiResponseBase> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        codeReceived.onCodeReceived(response.body());
                    } else {
                        codeReceived.onErrorReceived(getErrorByCode(response.body().getCode()));
                    }
                } else {
                    codeReceived.onErrorReceived(getErrorFromResponse(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponseBase> call, Throwable t) {
                t.printStackTrace();
                codeReceived.onErrorReceived(new Error(t));
//                codeReceived.onErrorReceived(new Error(context.getString(R.string.error_server_base)));
            }
        });
    }

    public void login(String phone, String code, String deviceId, String deviceName, final LoginComplete loginComplete) {
        Call<LoginResponse> call = api.login(phone, code, deviceId, deviceName, getLanguage());
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        loginComplete.onLoginComplete(response.body());
                    } else {
                        loginComplete.onErrorReceived(getErrorByCode(response.body().getCode()));
                    }
                } else {
                    loginComplete.onErrorReceived(getErrorFromResponse(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                t.printStackTrace();
                loginComplete.onErrorReceived(new Error(t));
//                loginComplete.onErrorReceived(new Error(context.getString(R.string.error_server_base)));
            }
        });
    }

    private String getLanguage() {
        return Locale.getDefault().getLanguage();
    }

    public void updateName(String name, final NameChanged nameChanged) {
        Call<NameResponse> call = api.updateName(this.authToken, name, getLanguage());
        call.enqueue(new Callback<NameResponse>() {
            @Override
            public void onResponse(Call<NameResponse> call, Response<NameResponse> response) {
                if (response.code() == 401 && unauthRequestHandler != null) {
                    unauthRequestHandler.onUnathRequest();
                    return;
                }
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        nameChanged.onNameChanged(response.body());
                    } else {
                        nameChanged.onErrorReceived(getErrorByCode(response.body().getCode()));
                    }
                } else {
                    nameChanged.onErrorReceived(getErrorFromResponse(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<NameResponse> call, Throwable t) {
                t.printStackTrace();
                nameChanged.onErrorReceived(new Error(context.getString(R.string.error_server_base)));
            }
        });
    }

    public void updateProfile(String firstName, String lastName, String patronymic,
                              String country, String city, final ProfileReceived profileReceived) {
        Call<ProfileResponse> call = api.updateProfile(this.authToken, firstName, lastName, patronymic, country, city, getLanguage());
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.code() == 401 && unauthRequestHandler != null) {
                    unauthRequestHandler.onUnathRequest();
                    return;
                }
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        profileReceived.onReceived(response.body());
                    } else {
                        profileReceived.onErrorReceived(getErrorByCode(response.body().getCode()));
                    }
                } else {
                    profileReceived.onErrorReceived(getErrorFromResponse(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                t.printStackTrace();
                profileReceived.onErrorReceived(new Error(context.getString(R.string.error_server_base)));
            }
        });
    }

    public void getProfile(final ProfileReceived profileReceived) {
        Call<ProfileResponse> call = api.getProfile(this.authToken, getLanguage());
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.code() == 401 && unauthRequestHandler != null) {
                    unauthRequestHandler.onUnathRequest();
                    return;
                }
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        profileReceived.onReceived(response.body());
                    } else {
                        profileReceived.onErrorReceived(getErrorByCode(response.body().getCode()));
                    }
                } else {
                    profileReceived.onErrorReceived(getErrorFromResponse(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                t.printStackTrace();
                profileReceived.onErrorReceived(new Error(context.getString(R.string.error_server_base)));
            }
        });
    }

    public void updateCompensationInfo(String firstName, String lastName, String patronymic,
                                       String birthday, String passport, String itn, String inila,
                                       String city, String street, String number, String apartment,
                                       String postcode, final BaseRequestCompleted requestCompleted) {
        Call<ApiResponseBase> call = api.updateCompensationInfo(this.authToken, firstName, lastName, patronymic, birthday, passport, itn, inila, city, street, number, apartment, postcode);
        call.enqueue(new Callback<ApiResponseBase>() {
            @Override
            public void onResponse(Call<ApiResponseBase> call, Response<ApiResponseBase> response) {
                if (response.code() == 401 && unauthRequestHandler != null) {
                    unauthRequestHandler.onUnathRequest();
                    return;
                }
                if (response.isSuccessful()) {
                    if (response.code() == 204) {
                        requestCompleted.onComplete(response.body());
                    } else {
                        requestCompleted.onErrorReceived(getErrorByCode(response.body().getCode()));
                    }
                } else {
                    requestCompleted.onErrorReceived(getErrorFromResponse(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponseBase> call, Throwable t) {
                t.printStackTrace();
                requestCompleted.onErrorReceived(new Error(context.getString(R.string.error_server_base)));
            }
        });
    }

    public void updateAvailability(final AvailabilityReceived millisecondsReceived) {
        Call<AvailabilityResponse> call = api.updateAvailability(this.authToken, getLanguage());
        call.enqueue(new Callback<AvailabilityResponse>() {
            @Override
            public void onResponse(Call<AvailabilityResponse> call, Response<AvailabilityResponse> response) {
                if (response.code() == 401 && unauthRequestHandler != null) {
                    unauthRequestHandler.onUnathRequest();
                    return;
                }
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        millisecondsReceived.onAvailabilityReceived(response.body());
                    } else {
                        millisecondsReceived.onErrorReceived(getErrorByCode(response.body().getCode()));
                    }
                } else {
                    millisecondsReceived.onErrorReceived(getErrorFromResponse(response.errorBody()));
                }

            }

            @Override
            public void onFailure(Call<AvailabilityResponse> call, Throwable t) {
                t.printStackTrace();
                millisecondsReceived.onErrorReceived(new Error(context.getString(R.string.error_server_base)));
            }
        });
    }

    public void logout(final BaseRequestCompleted baseRequestCompleted) {
        Call<ApiResponseBase> call = api.logout(this.authToken, getLanguage());
        call.enqueue(new Callback<ApiResponseBase>() {
            @Override
            public void onResponse(Call<ApiResponseBase> call, Response<ApiResponseBase> response) {
                if (response.code() == 401 && unauthRequestHandler != null) {
                    unauthRequestHandler.onUnathRequest();
                    return;
                }
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        baseRequestCompleted.onComplete(response.body());
                    } else {
                        baseRequestCompleted.onErrorReceived(getErrorByCode(response.body().getCode()));
                    }
                } else {
                    baseRequestCompleted.onErrorReceived(getErrorFromResponse(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponseBase> call, Throwable t) {
                t.printStackTrace();
                baseRequestCompleted.onErrorReceived(new Error(context.getString(R.string.error_server_base)));
            }
        });
    }


    public void makeCall(String reason, final TokenReceived tokenReceived) {
        Call<TokenResponse> call = api.makeCall(this.authToken, reason, getLanguage());
        call.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if (response.code() == 401 && unauthRequestHandler != null) {
                    unauthRequestHandler.onUnathRequest();
                    return;
                }
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        tokenReceived.onTokenReceived(response.body());
                    } else {
                        tokenReceived.onErrorReceived(getErrorByCode(response.body().getCode()));
                    }
                } else {
                    tokenReceived.onErrorReceived(getErrorFromResponse(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                t.printStackTrace();
                tokenReceived.onErrorReceived(new Error(context.getString(R.string.error_server_base)));
            }
        });
    }

    public void sendMessage(String sessionId, String message, final MessageSent messageSent) {
        Call<ApiResponseBase> call = api.sendMessage(this.authToken, sessionId, message, getLanguage());
        call.enqueue(new Callback<ApiResponseBase>() {
            @Override
            public void onResponse(Call<ApiResponseBase> call, Response<ApiResponseBase> response) {
                if (response.code() == 401 && unauthRequestHandler != null) {
                    unauthRequestHandler.onUnathRequest();
                    return;
                }
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        messageSent.onMessageSent(response.body());
                    } else {
                        messageSent.onErrorReceived(getErrorByCode(response.body().getCode()));
                    }
                } else {
                    messageSent.onErrorReceived(getErrorFromResponse(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponseBase> call, Throwable t) {
                t.printStackTrace();
                messageSent.onErrorReceived(new Error(context.getString(R.string.error_server_base)));
            }
        });
    }

    public void getTariffs(final TariffsReceived tariffsReceived) {
        Call<TariffsResponse> call = api.getTariffs(authToken, getLanguage());
        call.enqueue(new Callback<TariffsResponse>() {
            @Override
            public void onResponse(Call<TariffsResponse> call, Response<TariffsResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        tariffsReceived.onTariffsReceived(response.body());
                    } else {
                        tariffsReceived.onErrorReceived(getErrorByCode(response.body().getCode()));
                    }
                } else {
                    tariffsReceived.onErrorReceived(getErrorFromResponse(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<TariffsResponse> call, Throwable t) {
                t.printStackTrace();
                tariffsReceived.onErrorReceived(new Error(context.getString(R.string.error_server_base)));
            }
        });
    }

    public void getTariffsV2(final TariffsReceivedV2 tariffsReceived) {
        Call<TariffsResponseV2> call = api.getTariffsV2(authToken, getLanguage());
        call.enqueue(new Callback<TariffsResponseV2>() {
            @Override
            public void onResponse(Call<TariffsResponseV2> call, Response<TariffsResponseV2> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        tariffsReceived.onTariffsReceived(response.body());
                    } else {
                        tariffsReceived.onErrorReceived(getErrorByCode(response.body().getCode()));
                    }
                } else {
                    tariffsReceived.onErrorReceived(getErrorFromResponse(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<TariffsResponseV2> call, Throwable t) {
                t.printStackTrace();
                tariffsReceived.onErrorReceived(new Error(context.getString(R.string.error_server_base)));
            }
        });
    }

    public void buyYandexKassa(String token, String tariffId, final YandexKassaPaymentReceived buyReceived) {
        Call<CreateYandexPaymentResponse> call = api.createYandexPayment(authToken, token, tariffId, getLanguage());
        call.enqueue(new Callback<CreateYandexPaymentResponse>() {
            @Override
            public void onResponse(Call<CreateYandexPaymentResponse> call, Response<CreateYandexPaymentResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        buyReceived.onPaymentReceived(response.body());
                    }
                } else {
                    buyReceived.onErrorReceived(getErrorFromResponse(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<CreateYandexPaymentResponse> call, Throwable t) {
                t.printStackTrace();
                buyReceived.onErrorReceived(new Error(context.getString(R.string.error_server_base)));
            }
        });
    }

    public void approveYandexPayment(String paymentId, final YandexPaymentApprove yandexPaymentApprove) {
        Call<ApiResponseBase> call = api.approveYandexPayment(this.authToken, paymentId, getLanguage());
        call.enqueue(new Callback<ApiResponseBase>() {
            @Override
            public void onResponse(Call<ApiResponseBase> call, Response<ApiResponseBase> response) {
                if (response.code() == 401 && unauthRequestHandler != null) {
                    unauthRequestHandler.onUnathRequest();
                    return;
                }
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        yandexPaymentApprove.onYandexPaymentApprove(response.body());
                    } else {
                        yandexPaymentApprove.onErrorReceived(getErrorByCode(response.body().getCode()));
                    }
                } else {
                    yandexPaymentApprove.onErrorReceived(getErrorFromResponse(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponseBase> call, Throwable t) {
                t.printStackTrace();
                yandexPaymentApprove.onErrorReceived(new Error(context.getString(R.string.error_server_base)));
            }
        });
    }

    public void buyUnlim(String tariff, final LiqPayDataReceived dataReceived) {
        Call<LiqPayResponse> call = api.buyUnlim(this.authToken, tariff, getLanguage());
        call.enqueue(new Callback<LiqPayResponse>() {
            @Override
            public void onResponse(Call<LiqPayResponse> call, Response<LiqPayResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        dataReceived.onDataReceived(response.body());
                    } else {
                        dataReceived.onErrorReceived(getErrorByCode(response.body().getCode()));
                    }
                } else {
                    dataReceived.onErrorReceived(getErrorFromResponse(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<LiqPayResponse> call, Throwable t) {
                t.printStackTrace();
                dataReceived.onErrorReceived(new Error(context.getString(R.string.error_server_base)));
            }
        });
    }

    public void sendNotificationToken(String token) {
        Call<ApiResponseBase> call = api.sendNotificationToken(this.authToken, token);
        call.enqueue(new Callback<ApiResponseBase>() {
            @Override
            public void onResponse(Call<ApiResponseBase> call, Response<ApiResponseBase> response) {

            }

            @Override
            public void onFailure(Call<ApiResponseBase> call, Throwable t) {

            }
        });
    }

    public void getAppVersion(final AppVersionReceived appVersionReceived) {
        Call<AppVersionResponse> call = api.getAppVersion("android", getLanguage());
        call.enqueue(new Callback<AppVersionResponse>() {
            @Override
            public void onResponse(Call<AppVersionResponse> call, Response<AppVersionResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        appVersionReceived.onAppVersionReceived(response.body());
                    } else {
                        appVersionReceived.onErrorReceived(getErrorByCode(response.body().getCode()));
                    }
                } else {
                    appVersionReceived.onErrorReceived(getErrorFromResponse(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<AppVersionResponse> call, Throwable t) {
                t.printStackTrace();
                appVersionReceived.onErrorReceived(new Error(context.getString(R.string.error_server_base)));
            }
        });
    }

    public void sendUtogInfo(String firstName, String lastName, String patronymic, String memberId, final UtogResponseReceived utogResponseReceived) {
        Call<UtogResponse> call = api.sendUtogInfo(this.authToken, firstName, lastName, patronymic, memberId, getLanguage());
        call.enqueue(new Callback<UtogResponse>() {
            @Override
            public void onResponse(Call<UtogResponse> call, Response<UtogResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        utogResponseReceived.onInfoReceived(response.body());
                    } else {
                        utogResponseReceived.onErrorReceived(getErrorByCode(response.body().getCode()));
                    }
                } else {
                    utogResponseReceived.onErrorReceived(getErrorFromResponse(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<UtogResponse> call, Throwable t) {
                t.printStackTrace();
                utogResponseReceived.onErrorReceived(new Error(context.getString(R.string.error_server_base)));
            }
        });
    }

    public void getCountries(final CountriesReceived countriesReceived) {
        Call<CountriesResponse> call = api.getCountries(getLanguage());
        call.enqueue(new Callback<CountriesResponse>() {
            @Override
            public void onResponse(Call<CountriesResponse> call, Response<CountriesResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        countriesReceived.onCountriesReceived(response.body());
                    } else {
                        countriesReceived.onErrorReceived(getErrorByCode(response.body().getCode()));
                    }
                } else {
                    countriesReceived.onErrorReceived(getErrorFromResponse(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<CountriesResponse> call, Throwable t) {
                t.printStackTrace();
                countriesReceived.onErrorReceived(new Error(context.getString(R.string.error_server_base)));
            }
        });
    }

    public void getReasons(final ReasonsReceived reasonsReceived) {
        Call<ReasonsResponse> call = api.getReasons(getLanguage());
        call.enqueue(new Callback<ReasonsResponse>() {
            @Override
            public void onResponse(Call<ReasonsResponse> call, Response<ReasonsResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        reasonsReceived.onReasonsReceived(response.body());
                    } else {
                        reasonsReceived.onErrorReceived(getErrorByCode(response.body().getCode()));
                    }
                } else {
                    reasonsReceived.onErrorReceived(getErrorFromResponse(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ReasonsResponse> call, Throwable t) {
                t.printStackTrace();
                reasonsReceived.onErrorReceived(new Error(context.getString(R.string.error_server_base)));
            }
        });
    }

    public void leaveReview(String sessionId, int rate, String review) {
        Call<ApiResponseBase> call = api.leaveReview(authToken, sessionId, rate, review);
        call.enqueue(new Callback<ApiResponseBase>() {
            @Override
            public void onResponse(Call<ApiResponseBase> call, Response<ApiResponseBase> response) {

            }

            @Override
            public void onFailure(Call<ApiResponseBase> call, Throwable t) {

            }
        });
    }

    public void loginHelp(String phone, final HelpRequested helpRequested) {
        Call<ApiResponseBase> call = api.loginHelp(phone);
        call.enqueue(new Callback<ApiResponseBase>() {
            @Override
            public void onResponse(Call<ApiResponseBase> call, Response<ApiResponseBase> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        helpRequested.onHelpRequested();
                    } else {
                        helpRequested.onErrorReceived(getErrorByCode(response.body().getCode()));
                    }
                } else {
                    helpRequested.onErrorReceived(getErrorFromResponse(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponseBase> call, Throwable t) {
                t.printStackTrace();
                helpRequested.onErrorReceived(new Error(context.getString(R.string.error_server_base)));
            }
        });
    }

    public void loadRequests(final RequestsReceived requestsReceived) {
        Call<RequestsResponse> call = api.getRequests(authToken);
        call.enqueue(new Callback<RequestsResponse>() {
            @Override
            public void onResponse(Call<RequestsResponse> call, Response<RequestsResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        requestsReceived.onRequestsReceived(response.body());
                    } else {
                        requestsReceived.onErrorReceived(getErrorByCode(response.body().getCode()));
                    }
                } else {
                    requestsReceived.onErrorReceived(getErrorFromResponse(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<RequestsResponse> call, Throwable t) {
                requestsReceived.onErrorReceived(new Error(context.getString(R.string.error_server_base)));
            }
        });
    }

    public void newRequest(Bitmap image, final NewRequestCreated newRequestCreated) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), byteArray);
        MultipartBody.Part part = MultipartBody.Part.createFormData("media", "image", reqFile);

        Call<NewRequestResponse> call = api.newRequest(authToken, part);
        call.enqueue(new Callback<NewRequestResponse>() {
            @Override
            public void onResponse(Call<NewRequestResponse> call, Response<NewRequestResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        newRequestCreated.onRequestCreated(response.body());
                    } else {
                        newRequestCreated.onErrorReceived(getErrorByCode(response.body().getCode()));
                    }
                } else {
                    newRequestCreated.onErrorReceived(getErrorFromResponse(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<NewRequestResponse> call, Throwable t) {
                newRequestCreated.onErrorReceived(new Error(context.getString(R.string.error_server_base)));
            }
        });
    }

    public void loadRequestMessages(Request request, final RequestMessagesReceived messagesReceived) {
        Call<MessagesResponse> call = api.getRequestMessages(authToken, request.getId());
        call.enqueue(new Callback<MessagesResponse>() {
            @Override
            public void onResponse(Call<MessagesResponse> call, Response<MessagesResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        messagesReceived.onMessagesReceived(response.body());
                    } else {
                        messagesReceived.onErrorReceived(getErrorByCode(response.body().getCode()));
                    }
                } else {
                    messagesReceived.onErrorReceived(getErrorFromResponse(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<MessagesResponse> call, Throwable t) {
                messagesReceived.onErrorReceived(new Error(context.getString(R.string.error_server_base)));
            }
        });
    }

    public void newRequestMessage(Bitmap image, Request request, final NewRequestMessageCreated newMessageCreated) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), byteArray);
        MultipartBody.Part part = MultipartBody.Part.createFormData("media", "image", reqFile);
        RequestBody type = RequestBody.create(MediaType.parse("text/plain"), "media");
        Call<NewMessageResponse> call = api.newRequestMessage(authToken, request.getId(), part, type);
        call.enqueue(new Callback<NewMessageResponse>() {
            @Override
            public void onResponse(Call<NewMessageResponse> call, Response<NewMessageResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        newMessageCreated.onMessageCreated(response.body());
                    } else {
                        newMessageCreated.onErrorReceived(getErrorByCode(response.body().getCode()));
                    }
                } else {
                    newMessageCreated.onErrorReceived(getErrorFromResponse(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<NewMessageResponse> call, Throwable t) {
                newMessageCreated.onErrorReceived(new Error(context.getString(R.string.error_server_base)));
            }
        });
    }

    public void connectToGroupSession(String sessionId, final GroupSessionReceived received) {
        Call<GroupSessionResponse> call = api.connectToGroupSession(authToken, sessionId);
        call.enqueue(new Callback<GroupSessionResponse>() {
            @Override
            public void onResponse(Call<GroupSessionResponse> call, Response<GroupSessionResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        received.onDataReceived(response.body());
                    } else {
                        received.onErrorReceived(getErrorByCode(response.body().getCode()));
                    }
                } else {
                    received.onErrorReceived(getErrorFromResponse(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<GroupSessionResponse> call, Throwable t) {
                received.onErrorReceived(new Error(context.getString(R.string.error_server_base)));
            }
        });
    }

    public void askQuestion(String sessionId, final BaseRequestCompleted baseRequestCompleted) {
        Call<ApiResponseBase> call = api.askQuestion(authToken, sessionId);
        call.enqueue(new Callback<ApiResponseBase>() {
            @Override
            public void onResponse(Call<ApiResponseBase> call, Response<ApiResponseBase> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        baseRequestCompleted.onComplete(response.body());
                    } else {
                        baseRequestCompleted.onErrorReceived(getErrorByCode(response.body().getCode()));
                    }
                } else {
                    baseRequestCompleted.onErrorReceived(getErrorFromResponse(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponseBase> call, Throwable t) {
                baseRequestCompleted.onErrorReceived(new Error(t));
            }
        });
    }

    public void stopAsking(String sessionId, final BaseRequestCompleted baseRequestCompleted) {
        Call<ApiResponseBase> call = api.stopAsking(authToken, sessionId);
        call.enqueue(new Callback<ApiResponseBase>() {
            @Override
            public void onResponse(Call<ApiResponseBase> call, Response<ApiResponseBase> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        baseRequestCompleted.onComplete(response.body());
                    } else {
                        baseRequestCompleted.onErrorReceived(getErrorByCode(response.body().getCode()));
                    }
                } else {
                    baseRequestCompleted.onErrorReceived(getErrorFromResponse(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponseBase> call, Throwable t) {
                baseRequestCompleted.onErrorReceived(new Error(t));
            }
        });
    }

    public void getNews(final NewsReceived newsReceived) {
        Call<NewsResponse> call = api.getNews(authToken, getLanguage());
        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        newsReceived.onDataReceived(response.body());
                    } else {
                        newsReceived.onErrorReceived(getErrorByCode(response.body().getCode()));
                    }
                } else {
                    newsReceived.onErrorReceived(getErrorFromResponse(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                newsReceived.onErrorReceived(new Error(t));
            }
        });
    }


    private Error getErrorFromResponse(ResponseBody errorBody) {
        Converter<ResponseBody, ApiResponseBase> converter = retrofit.responseBodyConverter(ApiResponseBase.class, new Annotation[0]);
        try {
            ApiResponseBase responseBase = converter.convert(errorBody);
            return new Error(responseBase.getMessage());
        } catch (IOException e) {
        }
        return new Error(context.getString(R.string.error_server_base));
    }


    private Error getErrorByCode(int code) {
        switch (code) {
            case 8000:
                return new Error(context.getString(R.string.error_server_base));
            case 8001:
                return new Error(context.getString(R.string.error_validation));
            case 8002:
                return new Error(context.getString(R.string.error_login));
            case 8003:
                return new Error(context.getString(R.string.error_outside_working_time));
            case 8007:
                return new Error(context.getString(R.string.error_in_call));
            case 8008:
                return new Error(context.getString(R.string.error_no_right));

            default:
                return new Error(context.getString(R.string.error_server_base));
        }
    }

}
