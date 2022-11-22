package com.beatrice.ambulanceaid;

public class ModelAmubulance {
    private String accountType, email, name, online, phoneNumber, uid;

    public ModelAmubulance(){

    }

    public ModelAmubulance(String accountType, String email, String name, String online, String phoneNumber, String uid) {
        this.accountType = accountType;
        this.email = email;
        this.name = name;
        this.online = online;
        this.phoneNumber = phoneNumber;
        this.uid = uid;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
