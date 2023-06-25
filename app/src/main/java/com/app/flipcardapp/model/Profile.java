package com.app.flipcardapp.model;

public class Profile {
    private String Name;
    private String dOfBirth;
    private String mobileNumber;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getdOfBirth() {
        return dOfBirth;
    }

    public void setdOfBirth(String dOfBirth) {
        this.dOfBirth = dOfBirth;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
