package com.getbewarned.connectinterpreter;

import java.util.HashSet;
import java.util.Set;
import ru.yoo.sdk.kassa.payments.PaymentMethodType;

public class YandexKassaDataHolder {

    private final static String SHOP_ID = "741630";
    private final static String DEBUG_KEY = "test_NzQxNjMwoLR6B3SJeIm3BOLA7-nZeN2u8lIGrhsMAoM";
    private final static String PROD_KEY = "live_NzQwMDMxYoxWXoO4aSV_nXYPCznd9V32-Xv3-BdpZa8";
    public static String tariffId;
    public static String yandexPurchaseId;

    public static String getClientApplicationKey() {
        return PROD_KEY;
    }

    public static String getShopId() {
        return SHOP_ID;
    }

    public static void initDefault() {
        tariffId = null;
        yandexPurchaseId = null;
    }

    public static Set<PaymentMethodType> getPayMethods() {
        HashSet<PaymentMethodType> methods = new HashSet<>();
        methods.add(PaymentMethodType.BANK_CARD);
        methods.add(PaymentMethodType.SBERBANK);
        methods.add(PaymentMethodType.GOOGLE_PAY);
        return methods;
    }
}
