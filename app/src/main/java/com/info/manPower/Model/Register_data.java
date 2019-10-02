package com.info.manPower.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Register_data
{
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("f_name")
    @Expose
    private String fName;
    @SerializedName("l_name")
    @Expose
    private String lName;
    @SerializedName("mobile_no")
    @Expose
    private String mobileNo;
    @SerializedName("user_email")
    @Expose
    private String userEmail;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("status")
    @Expose
    private String status;

    public Register_data(String userId, String fName, String lName, String mobileNo, String userEmail, String address, String gender, String password, String status) {
        this.userId = userId;
        this.fName = fName;
        this.lName = lName;
        this.mobileNo = mobileNo;
        this.userEmail = userEmail;
        this.address = address;
        this.gender = gender;
        this.password = password;
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
