package com.getbewarned.connectinterpreter.presenters;

import android.content.Context;
import android.os.Bundle;

import com.getbewarned.connectinterpreter.R;
import com.getbewarned.connectinterpreter.adapters.TariffsAdapter;
import com.getbewarned.connectinterpreter.interfaces.Presenter;
import com.getbewarned.connectinterpreter.interfaces.PurchaseView;
import com.getbewarned.connectinterpreter.interfaces.TariffClickListener;
import com.getbewarned.connectinterpreter.interfaces.TariffsReceived;
import com.getbewarned.connectinterpreter.managers.NetworkManager;
import com.getbewarned.connectinterpreter.managers.UserManager;
import com.getbewarned.connectinterpreter.models.TariffItem;
import com.getbewarned.connectinterpreter.models.TariffResponse;
import com.getbewarned.connectinterpreter.models.TariffsResponse;

import java.util.ArrayList;
import java.util.List;

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
}
