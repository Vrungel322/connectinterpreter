package com.getbewarned.connectinterpreter.presenters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.getbewarned.connectinterpreter.YandexKassaDataHolder;
import com.getbewarned.connectinterpreter.adapters.TariffsAdapter;
import com.getbewarned.connectinterpreter.analytics.Analytics;
import com.getbewarned.connectinterpreter.analytics.Events;
import com.getbewarned.connectinterpreter.interfaces.Presenter;
import com.getbewarned.connectinterpreter.interfaces.PurchaseView;
import com.getbewarned.connectinterpreter.interfaces.TariffClickListener;
import com.getbewarned.connectinterpreter.interfaces.TariffsReceivedV2;
import com.getbewarned.connectinterpreter.interfaces.YandexKassaPaymentReceived;
import com.getbewarned.connectinterpreter.interfaces.YandexPaymentApprove;
import com.getbewarned.connectinterpreter.managers.NetworkManager;
import com.getbewarned.connectinterpreter.managers.UserManager;
import com.getbewarned.connectinterpreter.models.ApiResponseBase;
import com.getbewarned.connectinterpreter.models.CreateYandexPaymentResponse;
import com.getbewarned.connectinterpreter.models.TariffItem;
import com.getbewarned.connectinterpreter.models.TariffResponse;
import com.getbewarned.connectinterpreter.models.TariffsResponseV2;

import java.util.ArrayList;
import java.util.List;

public class PurchasePresenter implements Presenter, TariffClickListener {

    private final PurchaseView view;
    NetworkManager networkManager;
    private final TariffsAdapter adapter;
    private final Context context;

    public PurchasePresenter(final PurchaseView view, Context context) {
        this.view = view;
        this.context = context;
        UserManager userManager = new UserManager(context);
        networkManager = new NetworkManager(context);
        networkManager.setAuthToken(userManager.getUserToken());

        adapter = new TariffsAdapter(this);
    }

    public TariffsAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void onCreate(Bundle extras) {
        networkManager.getTariffsV2(new TariffsReceivedV2() {
            @Override
            public void onTariffsReceived(TariffsResponseV2 response) {
                final List<TariffItem> items = new ArrayList<>();
                for (TariffResponse item : response.getTariffs()) {
                    items.add(new TariffItem(item.getName(), item.getPrice(), item.getMinutes(), item.getId(), item.getCurrencySign(), item.getCurrency()));
                }
                adapter.setItems(items);
            }

            @Override
            public void onErrorReceived(Error error) {
                view.error(error);
            }
        });
    }

    @Override
    public void onTariffClick(TariffItem item) {
        view.updateDoneBtn();

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }

    public TariffItem getSelectedTariff() {
        return adapter.selectedItem;
    }

    public void sendPaymentToken(String token, final String tariffId) {
        Log.d("YK", "payment token: " + token);
        Log.d("YK", "tariff: " + tariffId);
        Analytics.getInstance().trackEvent(Events.EVENT_PURCHASE_PLAN + tariffId);
        networkManager.buyYandexKassa(token, tariffId, new YandexKassaPaymentReceived() {
            @Override
            public void onPaymentReceived(CreateYandexPaymentResponse response) {
                String confirmation = response.getConfirmation();
                if (confirmation != null && !confirmation.isEmpty()) {
                    YandexKassaDataHolder.yandexPurchaseId = response.getId();
                    view.start3DSecure(confirmation);
                } else {
                    approvePayment(response.getId(), tariffId);
                }
            }

            @Override
            public void onErrorReceived(Error error) {
                Analytics.getInstance().trackEvent(Events.EVENT_PURCHASE_PLAN_ERROR + tariffId);
                view.error(error);
            }
        });
    }

    public void approvePayment(String yandexPurchaseId, final String tariffId) {
        networkManager.approveYandexPayment(yandexPurchaseId, new YandexPaymentApprove() {
            @Override
            public void onYandexPaymentApprove(ApiResponseBase response) {
                Analytics.getInstance().trackEvent(Events.EVENT_PURCHASE_PLAN_SUCCESS + tariffId);
                view.paymentSuccess();
            }

            @Override
            public void onErrorReceived(Error error) {
                Analytics.getInstance().trackEvent(Events.EVENT_PURCHASE_PLAN_ERROR + tariffId);
                view.error(error);
            }
        });
    }
}
