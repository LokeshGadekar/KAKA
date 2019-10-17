package com.kaka.ics.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Registration_Responce {

    @SerializedName("responce")
    @Expose
    private Boolean responce;
    @SerializedName("data")
    @Expose
    private Register_data data;

    public Boolean getResponce() {
        return responce;
    }

    public void setResponce(Boolean responce) {
        this.responce = responce;
    }

    public Register_data getData() {
        return data;
    }

    public void setData(Register_data data) {
        this.data = data;
    }

}
