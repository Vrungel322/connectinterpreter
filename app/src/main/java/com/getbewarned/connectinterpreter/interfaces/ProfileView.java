package com.getbewarned.connectinterpreter.interfaces;

public interface ProfileView {
    void navigateToLogin();

    void updateUserData(String userName, String userLastName, String userPatronymic, String userCountry, String userCity, String userPhone);
}
