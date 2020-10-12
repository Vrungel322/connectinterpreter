package com.getbewarned.connectinterpreter.interfaces

interface EditPersonalInfoView {
    fun navigateToLogin()
    fun showUsedData(userPhone: String?, userName: String?, userLastName: String?, userPatronymic: String?, userCountry: String?, userCity: String?)
    fun showError(message: String)
    fun goBack()
}