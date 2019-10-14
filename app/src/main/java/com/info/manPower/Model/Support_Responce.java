package com.info.manPower.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Support_Responce
{
    @SerializedName("data")
    @Expose
    private Support_data data;

    @SerializedName("responce")
    @Expose
    private Boolean responce;

    public Support_data getData() {
        return data;
    }

    public void setData(Support_data data) {
        this.data = data;
    }

    public Boolean getResponce() {
        return responce;
    }

    public void setResponce(Boolean responce) {
        this.responce = responce;
    }

}