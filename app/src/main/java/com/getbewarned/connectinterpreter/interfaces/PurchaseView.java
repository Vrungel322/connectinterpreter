package com.getbewarned.connectinterpreter.interfaces;

public interface PurchaseView {
    void error(Error error);

    void updateDoneBtn();

    void successPurchase();

    void failPurchase(String errorMsg);

    void start3DSecure(String confirmationUrl);

    void paymentSuccess();

    void setSignText(String sign);
}
