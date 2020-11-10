package com.getbewarned.connectinterpreter.presenters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.getbewarned.connectinterpreter.adapters.TariffsAdapter;
import com.getbewarned.connectinterpreter.interfaces.LiqPayDataReceived;
import com.getbewarned.connectinterpreter.interfaces.Presenter;
import com.getbewarned.connectinterpreter.interfaces.PurchaseView;
import com.getbewarned.connectinterpreter.interfaces.TariffClickListener;
import com.getbewarned.connectinterpreter.interfaces.TariffsReceivedV2;
import com.getbewarned.connectinterpreter.managers.NetworkManager;
import com.getbewarned.connectinterpreter.managers.UserManager;
import com.getbewarned.connectinterpreter.models.LiqPayResponse;
import com.getbewarned.connectinterpreter.models.TariffItem;
import com.getbewarned.connectinterpreter.models.TariffResponse;
import com.getbewarned.connectinterpreter.models.TariffsResponseV2;

import java.util.ArrayList;
import java.util.List;

import ua.privatbank.paylibliqpay.ErrorCode;
import ua.privatbank.paylibliqpay.LiqPay;
import ua.privatbank.paylibliqpay.LiqPayCallBack;

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
                    items.add(new TariffItem(item.getName(), item.getPrice(), item.getMinutes(), item.getId(), item.getCurrencySign()));
                }
                adapter.setItems(items);
            }

            @Override
            public void onErrorReceived(Error error) {
                view.errorReceivingTariffs(error);
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
}
