package com.getbewarned.connectinterpreter.presenters;

import android.os.Bundle;
import android.util.Log;

import com.getbewarned.connectinterpreter.interfaces.LiqPayDataReceived;
import com.getbewarned.connectinterpreter.interfaces.BaseRequestCompleted;
import com.getbewarned.connectinterpreter.interfaces.MainView;
import com.getbewarned.connectinterpreter.interfaces.AvailabilityReceived;
import com.getbewarned.connectinterpreter.interfaces.NameChanged;
import com.getbewarned.connectinterpreter.interfaces.Presenter;
import com.getbewarned.connectinterpreter.interfaces.ReasonsReceived;
import com.getbewarned.connectinterpreter.interfaces.TariffsReceived;
import com.getbewarned.connectinterpreter.interfaces.TokenReceived;
import com.getbewarned.connectinterpreter.interfaces.UnauthRequestHandler;
import com.getbewarned.connectinterpreter.managers.NetworkManager;
import com.getbewarned.connectinterpreter.managers.UserManager;
import com.getbewarned.connectinterpreter.models.ApiResponseBase;
import com.getbewarned.connectinterpreter.models.HumanDate;
import com.getbewarned.connectinterpreter.models.HumanTime;
import com.getbewarned.connectinterpreter.models.AvailabilityResponse;
import com.getbewarned.connectinterpreter.models.LiqPayResponse;
import com.getbewarned.connectinterpreter.models.NameResponse;
import com.getbewarned.connectinterpreter.models.Reason;
import com.getbewarned.connectinterpreter.models.ReasonResponse;
import com.getbewarned.connectinterpreter.models.ReasonsResponse;
import com.getbewarned.connectinterpreter.models.TariffsResponse;
import com.getbewarned.connectinterpreter.models.TokenResponse;
import com.getbewarned.connectinterpreter.models.UtogAsk;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import ua.privatbank.paylibliqpay.ErrorCode;
import ua.privatbank.paylibliqpay.LiqPay;
import ua.privatbank.paylibliqpay.LiqPayCallBack;

/**
 * Created by artycake on 10/11/17.
 */

public class MainPresenter implements Presenter {
    private MainView view;
    private UserManager userManager;
    private NetworkManager networkManager;
    private String selectedTariff;

    private boolean callInitiated = false;

    public MainPresenter(final MainView view) {
        this.view = view;
        this.userManager = new UserManager(view.getContext());
        this.networkManager = new NetworkManager(view.getContext());
        this.networkManager.setAuthToken(userManager.getUserToken());
        this.networkManager.setUnauthRequestHandler(new UnauthRequestHandler() {
            @Override
            public void onUnathRequest() {
                userManager.updateUserToken("");
                view.navigateToLogin();
            }
        });
    }

    @Override
    public void onCreate(Bundle extras) {
        view.showChecking();
        view.showLeftTime("00:00");
        view.requestPermissions();
        view.showWorkTime(userManager.getUserUkrainian());
        String fbToken = FirebaseInstanceId.getInstance().getToken();
        if (fbToken != null) {
            Log.d("FB_TOKEN", fbToken);
            networkManager.sendNotificationToken(fbToken);
        }
    }

    public void onPermissionsGranted() {
        String name = userManager.getUserName();
        if (name.isEmpty()) {
            view.askForName(null);
        } else {
            view.updateUserName(name);
        }
    }

    public void onPause() {
    }

    public void onResume() {
        view.showChecking();
        updateAvailability();
        checkUtog();

        if (userManager.getLastCallSessionId() != null) {
            view.askAboutLastCall();
        }
    }

    private void checkUtog() {
        if (!userManager.getUserUkrainian() || userManager.getUserUtog()) {
            return;
        }

        switch (userManager.getUtogAsk().getStatus()) {
            case NEVER:
                return;
            case LATER:
                if (new Date().before(userManager.getUtogAsk().getDate())) {
                    return;
                }
            case SHOUD_ASK:
            default:
                view.askAboutUtog();
        }
    }

    public void utogConfirmed() {
        utogLater();
        view.navigateToUtog();
    }

    public void utogLater() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        userManager.updateUtogAsk(new UtogAsk(UtogAsk.STATUS.LATER, calendar.getTime()));
    }

    public void utogNever() {
        userManager.updateUtogAsk(new UtogAsk(UtogAsk.STATUS.NEVER, null));
    }

    private void updateAvailability() {
        networkManager.updateAvailability(new AvailabilityReceived() {
            @Override
            public void onAvailabilityReceived(AvailabilityResponse response) {
                if (response.isSuccess()) {
                    userManager.updateUserSeconds(response.getSeconds());
                    userManager.updateUserUnlim(response.isUnlim());
                    userManager.updateUserActiveTill(response.getActiveTill());
                    userManager.updateUserUtog(response.isUtog());
                    view.toggleUnlim(userManager.getUserUnlim());
                    if (!userManager.getUserUkrainian() || userManager.getUserUtog()) {
                        view.hideUtog();
                    }
                    if (response.isUnlim()) {
                        HumanDate humanDate = new HumanDate(view.getContext(), response.getActiveTill());
                        view.toggleCallAvailability(true, userManager.getUserUtog());
                        view.showDateTill(humanDate.getDate());
                    } else {
                        HumanTime humanTime = new HumanTime(response.getSeconds() * 1000);
                        if (response.getSeconds() > 0) {
                            view.toggleCallAvailability(true, userManager.getUserUtog());
                            view.showLeftTime(humanTime.getTime());
                        } else {
                            view.toggleCallAvailability(false, userManager.getUserUtog());
                        }
                    }
                } else {
                    view.showError(response.getMessage());
                }
            }

            @Override
            public void onErrorReceived(Error error) {
                view.showError(error.getMessage());
            }
        });
    }

    public void onDestroy() {
    }

    public void onStartCallPressed() {
        networkManager.getReasons(new ReasonsReceived() {
            @Override
            public void onReasonsReceived(ReasonsResponse response) {
                List<Reason> reasons = new ArrayList<>();
                for (ReasonResponse reasonResponse : response.getReasons()) {
                    reasons.add(new Reason(reasonResponse.getSlug(), reasonResponse.getLabel()));
                }
                view.askForReason(reasons);
            }

            @Override
            public void onErrorReceived(Error error) {
                view.showError(error.getMessage());
            }
        });

    }

    public void reasonSelected(final Reason reason) {

        if (callInitiated) {
            return;
        }
        callInitiated = true;
        networkManager.makeCall(reason.getSlug(), new TokenReceived() {
            @Override
            public void onTokenReceived(TokenResponse response) {
                view.navigateToCallWith(response.getToken(), response.getSessionId(), response.getApiKey(), response.getMaxSeconds());
                callInitiated = false;
            }

            @Override
            public void onErrorReceived(Error error) {
                callInitiated = false;
                view.showError(error.getMessage());
            }
        });
    }

    public void nameChanged(String name) {
        final String oldName = userManager.getUserName();
        view.updateUserName(name);
        networkManager.updateName(name, new NameChanged() {
            @Override
            public void onNameChanged(NameResponse response) {
                if (response.isSuccess()) {
                    userManager.updateUserName(response.getName());
                    view.updateUserName(response.getName());
                } else {
                    view.showError(response.getMessage());
                    view.updateUserName(oldName);
                }
            }

            @Override
            public void onErrorReceived(Error error) {
                view.showError(error.getMessage());
                view.updateUserName(oldName);
            }
        });
    }

    public void userNamePressed() {
        view.askForName(userManager.getUserName());
    }

    public void logout() {
        networkManager.logout(new BaseRequestCompleted() {
            @Override
            public void onComplete(ApiResponseBase response) {
                if (response.isSuccess()) {
                    view.navigateToLogin();
                    Realm realm = Realm.getDefaultInstance();
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.deleteAll();
                        }
                    });
                } else {
                    view.showError(response.getMessage());
                }
            }

            @Override
            public void onErrorReceived(Error error) {
                view.showError(error.getMessage());
            }
        });
    }

    public void buyUnlimPressed() {
        view.toggleBuyUnlimEnabled(false);
        networkManager.getTariffs(new TariffsReceived() {
            @Override
            public void onTariffsReceived(TariffsResponse response) {
                view.showTariffsSelector(response.getTariffs());
                view.toggleBuyUnlimEnabled(true);
            }

            @Override
            public void onErrorReceived(Error error) {
                view.toggleBuyUnlimEnabled(true);
            }
        });
    }

    public void tariffSelected(String tariff) {
        selectedTariff = tariff;
        view.requestLiqPayPermissions();
    }

    public void onLiqPayPermissionsGranted() {
        // show preloader
        view.toggleBuyUnlimEnabled(false);
        networkManager.buyUnlim(selectedTariff, new LiqPayDataReceived() {
            @Override
            public void onDataReceived(LiqPayResponse response) {
                LiqPay.checkout(view.getContext(), response.getData(), response.getSignature(), new LiqPayCallBack() {
                    @Override
                    public void onResponseSuccess(String s) {
                        Log.i("PAYMENT SUCCESS", s);
                        view.toggleBuyUnlimEnabled(true);
                    }

                    @Override
                    public void onResponceError(ErrorCode errorCode) {
                        Log.i("PAYMENT FAIL", errorCode.toString());
                        view.toggleBuyUnlimEnabled(true);
                    }
                });
            }

            @Override
            public void onErrorReceived(Error error) {
                Log.i("PAYMENT FAIL", error.getMessage());
                view.toggleBuyUnlimEnabled(true);
            }
        });
        selectedTariff = null;
    }

    public void onReview(int rating, String review) {
        String sessionId = userManager.getLastCallSessionId();
        networkManager.leaveReview(sessionId, rating, review);

        userManager.updateLastCallSessionId(null);
    }

    public void onReviewSkipped() {
        userManager.updateLastCallSessionId(null);
    }
}
