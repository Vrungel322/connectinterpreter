package com.getbewarned.connectinterpreter;

public class YandexKassaDataHolder {

    private final static String SHOP_ID = "741630";
    private final static String DEBUG_KEY = "test_NzQxNjMwoLR6B3SJeIm3BOLA7-nZeN2u8lIGrhsMAoM";
    private final static String PROD_KEY = "live_NzQwMDMxYoxWXoO4aSV_nXYPCznd9V32-Xv3-BdpZa8";

    public static String getClientApplicationKey() {
        return DEBUG_KEY;
    }

    public static String getShopId() {
        return SHOP_ID;
    }

}
