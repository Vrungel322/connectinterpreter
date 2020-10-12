package com.getbewarned.connectinterpreter.models;

import com.google.gson.annotations.Expose;

public class ProfileResponse extends ApiResponseBase{
    @Expose
    public Profile profile;
    @Expose
    public boolean compensation_prefilled;

    public boolean isSuccess() {
        return success;
    }

    public Profile getProfile() {
        return profile;
    }

    public boolean isCompensationPrefilled() {
        return compensation_prefilled;
    }

    public static class Profile{
        @Expose
        public int id;
        @Expose
        public String last_name;
        @Expose
        public String first_name;
        @Expose
        public String patronymic;
        @Expose
        public String gender;
        @Expose
        public String avatar;
        @Expose
        public int birthday;
        @Expose
        public String city;
        @Expose
        public String country;

        public int getId() {
            return id;
        }

        public String getLastName() {
            return last_name;
        }

        public String getFirstName() {
            return first_name;
        }

        public String getPatronymic() {
            return patronymic;
        }

        public String getGender() {
            return gender;
        }

        public String getAvatar() {
            return avatar;
        }

        public int getBirthday() {
            return birthday;
        }

        public String getCity() {
            return city;
        }

        public String getCountry() {
            return country;
        }
    }
}
