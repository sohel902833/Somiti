package com.example.somiti.Adapters;

public class Users {


    String name;
    String phone;
    String date;
    String time;
    String id;


    public Users(String name, String phone, String date, String time, String id) {
        this.name = name;
        this.phone = phone;
        this.date = date;
        this.time = time;
        this.id = id;
    }



    public  Users(){

    }




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
