package com.getbewarned.connectinterpreter.managers;

import android.content.Context;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.interfaces.ApiService;
import com.getbewarned.connectinterpreter.interfaces.AppVersionReceived;
import com.getbewarned.connectinterpreter.interfaces.CodeReceived;
import com.getbewarned.connectinterpreter.interfaces.CountriesReceived;
import com.getbewarned.connectinterpreter.interfaces.HelpRequested;
import com.getbewarned.connectinterpreter.interfaces.LiqPayDataReceived;
import com.getbewarned.connectinterpreter.interfaces.LoginComplete;
import com.getbewarned.connectinterpreter.interfaces.LogoutComplete;
import com.getbewarned.connectinterpreter.interfaces.MessageSent;
import com.getbewarned.connectinterpreter.interfaces.AvailabilityReceived;
import com.getbewarned.connectinterpreter.interfaces.NameChanged;
import com.getbewarned.connectinterpreter.interfaces.TariffsReceived;
import com.getbewarned.connectinterpreter.interfaces.TokenReceived;
import com.getbewarned.connectinterpreter.interfaces.UnauthRequestHandler;
import com.getbewarned.connectinterpreter.interfaces.UtogResponseReceived;
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
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Locale;

import okhttp3.OkHttpClient;
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

    private ApiService api;
    private String authToken;
    private Retrofit retrofit;
    private Context context;


    private UnauthRequestHandler unauthRequestHandler;

    public NetworkManager(Context context) {
        this.context = context;

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(loggingInterceptor);
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
                codeReceived.onErrorReceived(new Error(context.getString(R.string.error_server_base)));
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
                loginComplete.onErrorReceived(new Error(context.getString(R.string.error_server_base)));
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

    public void logout(final LogoutComplete logoutComplete) {
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
                        logoutComplete.onLogoutComplete(response.body());
                    } else {
                        logoutComplete.onErrorReceived(getErrorByCode(response.body().getCode()));
                    }
                } else {
                    logoutComplete.onErrorReceived(getErrorFromResponse(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponseBase> call, Throwable t) {
                t.printStackTrace();
                logoutComplete.onErrorReceived(new Error(context.getString(R.string.error_server_base)));
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
        Call<TariffsResponse> call = api.getTariffs(getLanguage());
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
