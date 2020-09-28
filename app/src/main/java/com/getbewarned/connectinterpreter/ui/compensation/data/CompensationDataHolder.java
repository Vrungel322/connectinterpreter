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
    public String insuranceId;
    public String city;
    public String streetAddress;
    public String buildingNumber;
    public String apartmentNumber;
    public String postId;

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
