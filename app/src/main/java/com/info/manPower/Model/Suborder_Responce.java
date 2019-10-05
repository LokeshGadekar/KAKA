package com.info.manPower.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Suborder_Responce
{
    @SerializedName("responce")
    @Expose
    private Boolean responce;
    @SerializedName("data")
    @Expose
    private List<Suborder_data> data = null;

    public Boolean getResponce() {
        return responce;
    }

    public void setResponce(Boolean responce) {
        this.responce = responce;
    }

    public List<Suborder_data> getData() {
        return data;
    }

    public void setData(List<Suborder_data> data) {
        this.data = data;
    }

}
