package com.example.somiti.Adapters;

import android.accounts.Account;

import com.google.firebase.database.connection.util.StringListReader;

public class AccountDetails {

    String amount;
    String date;
    String id;
    String mid;
    String name;
    String time;

    public AccountDetails(){

    }

    public AccountDetails(String amount, String date, String id, String mid, String name, String time) {
        this.amount = amount;
        this.date = date;
        this.id = id;
        this.mid = mid;
        this.name = name;
        this.time = time;
    }


    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
