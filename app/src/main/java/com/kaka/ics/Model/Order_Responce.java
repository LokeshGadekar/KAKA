package com.kaka.ics.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Order_Responce
{
    @SerializedName("responce")
    @Expose
    private Boolean responce;
    @SerializedName("data")
    @Expose
    private Order_Post data;

    public Boolean getResponce() {
        return responce;
    }

    public void setResponce(Boolean responce) {
        this.responce = responce;
    }

    public Order_Post getData() {
        return data;
    }

    public void setData(Order_Post data) {
        this.data = data;
    }
}
