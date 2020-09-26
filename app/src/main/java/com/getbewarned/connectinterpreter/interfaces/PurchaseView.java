package com.getbewarned.connectinterpreter.interfaces;

public interface PurchaseView {
    void errorReceivingTariffs(Error error);

    void updateDoneBtn();

    void successPurchase();

    void failPurchase(String errorMsg);
}
