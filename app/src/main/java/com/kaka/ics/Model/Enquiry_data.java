package com.kaka.ics.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Enquiry_data
{
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("worker_cat")
    @Expose
    private String workerCat;
    @SerializedName("cat_name")
    @Expose
    private String catName;
    @SerializedName("worker_subcat")
    @Expose
    private String workerSubcat;
    @SerializedName("worker_no")
    @Expose
    private String workerNo;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("e_d_h")
    @Expose
    private String eDH;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("mobile_no")
    @Expose
    private String mobileNo;
    @SerializedName("address_one")
    @Expose
    private String addressOne;
    @SerializedName("address_two")
    @Expose
    private String addressTwo;
    @SerializedName("date")
    @Expose
    private String date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWorkerCat() {
        return workerCat;
    }

    public void setWorkerCat(String workerCat) {
        this.workerCat = workerCat;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getWorkerSubcat() {
        return workerSubcat;
    }

    public void setWorkerSubcat(String workerSubcat) {
        this.workerSubcat = workerSubcat;
    }

    public String getWorkerNo() {
        return workerNo;
    }

    public void setWorkerNo(String workerNo) {
        this.workerNo = workerNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEDH() {
        return eDH;
    }

    public void setEDH(String eDH) {
        this.eDH = eDH;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getAddressOne() {
        return addressOne;
    }

    public void setAddressOne(String addressOne) {
        this.addressOne = addressOne;
    }

    public String getAddressTwo() {
        return addressTwo;
    }

    public void setAddressTwo(String addressTwo) {
        this.addressTwo = addressTwo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;}

}
