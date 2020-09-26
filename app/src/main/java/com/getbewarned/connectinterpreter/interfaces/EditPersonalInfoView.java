package com.getbewarned.connectinterpreter.interfaces;

public interface EditPersonalInfoView {
    void navigateToLogin();

    void showUsedData(String userPhone, String userName, String userLastName, String userPatronymic, String userCountry, String userCity);
}
