package com.kaka.ics.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cart_data
{
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("date_from")
    @Expose
    private String dateFrom;
    @SerializedName("d")
    @Expose
    private String d;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("date_to")
    @Expose
    private String dateTo;
    @SerializedName("subcat_name")
    @Expose
    private String subcatName;
    @SerializedName("time_from")
    @Expose
    private String timeFrom;
    @SerializedName("time_to")
    @Expose
    private String timeTo;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("sub_cat_no")
    @Expose
    private String subcatnum;
    @SerializedName("rate")
    @Expose
    private String rate;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public String getSubcatName() {
        return subcatName;
    }

    public void setSubcatName(String subcatName) {
        this.subcatName = subcatName;
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }

    public String getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(String timeTo) {
        this.timeTo = timeTo;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSubcatnum() {
        return subcatnum;
    }

    public void setSubcatnum(String subcatnum) {
        this.subcatnum = subcatnum;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}