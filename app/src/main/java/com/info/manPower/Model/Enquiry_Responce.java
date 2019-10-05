package com.info.manPower.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Enquiry_Responce
{
    @SerializedName("responce")
    @Expose
    private Boolean responce;
    @SerializedName("data")
    @Expose
    private Enquiry_data data;

    public Boolean getResponce() {
        return responce;
    }

    public void setResponce(Boolean responce) {
        this.responce = responce;
    }

    public Enquiry_data getData() {
        return data;
    }

    public void setData(Enquiry_data data) {
        this.data = data;
    }

}
