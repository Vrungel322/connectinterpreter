package com.getbewarned.connectinterpreter.presenters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.adapters.TariffsAdapter;
import com.getbewarned.connectinterpreter.interfaces.LiqPayDataReceived;
import com.getbewarned.connectinterpreter.interfaces.Presenter;
import com.getbewarned.connectinterpreter.interfaces.PurchaseView;
import com.getbewarned.connectinterpreter.interfaces.TariffClickListener;
import com.getbewarned.connectinterpreter.interfaces.TariffsReceived;
import com.getbewarned.connectinterpreter.managers.NetworkManager;
import com.getbewarned.connectinterpreter.managers.UserManager;
import com.getbewarned.connectinterpreter.models.LiqPayResponse;
import com.getbewarned.connectinterpreter.models.TariffItem;
import com.getbewarned.connectinterpreter.models.TariffResponse;
import com.getbewarned.connectinterpreter.models.TariffsResponse;

import java.util.ArrayList;
import java.util.List;

import ua.privatbank.paylibliqpay.ErrorCode;
import ua.privatbank.paylibliqpay.LiqPay;
import ua.privatbank.paylibliqpay.LiqPayCallBack;

public class PurchasePresenter implements Presenter, TariffClickListener {

    private PurchaseView view;
    NetworkManager networkManager;
    private TariffsAdapter adapter;
    private Context context;

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
        networkManager.getTariffs(new TariffsReceived() {
            @Override
            public void onTariffsReceived(TariffsResponse response) {
                final List<TariffItem> items = new ArrayList<>();
                for (TariffResponse item : response.getTariffs()) {
                    items.add(new TariffItem(context.getString(R.string.tariff_name_stub), item.getPrice(), item.getName(), item.getId()));
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

    public void startLiqPayPurchaseFlow() {
        networkManager.buyUnlim(adapter.selectedItem.tariffId, new LiqPayDataReceived() {
            @Override
            public void onDataReceived(LiqPayResponse response) {
                LiqPay.checkout(context, response.getData(), response.getSignature(), new LiqPayCallBack() {
                    @Override
                    public void onResponseSuccess(String s) {
                        Log.i("PAYMENT SUCCESS", s);
                        view.successPurchase();
                    }

                    @Override
                    public void onResponceError(ErrorCode errorCode) {
                        Log.i("PAYMENT FAIL", errorCode.toString());
                        view.failPurchase(errorCode.toString());
                    }
                });
            }

            @Override
            public void onErrorReceived(Error error) {
                Log.i("PAYMENT FAIL", error.getMessage());
                view.failPurchase(error.toString());
            }
        });
    }
}
