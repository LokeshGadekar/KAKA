package com.kaka.ics.Model;

public class Location_data
{
    String id;
    String city;
    String date;

    public Location_data(String id, String city, String date) {
        this.id = id;
        this.city = city;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
