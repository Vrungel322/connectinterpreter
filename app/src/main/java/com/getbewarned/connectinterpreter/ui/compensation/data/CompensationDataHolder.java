package com.getbewarned.connectinterpreter.ui.compensation.data;

import java.io.Serializable;

public class CompensationDataHolder implements Serializable {
    public String lastName;
    public String firstName;
    public String patronymic;
    public Long dateOfBirthMillis;
    public String passportSerialCode;
    public String passportSerialNumber;
    public String taxPayerId;

    private static CompensationDataHolder dataHolder;

    public static CompensationDataHolder getInstance() {
        if (dataHolder == null) {
            dataHolder = new CompensationDataHolder();
        }
        return dataHolder;
    }

    public static void dispose() {
        dataHolder = null;
    }

}
