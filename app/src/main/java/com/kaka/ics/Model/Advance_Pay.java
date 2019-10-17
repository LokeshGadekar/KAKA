package com.kaka.ics.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Advance_Pay
{
    @SerializedName("payment_advance")
    @Expose
    private Integer paymentAdvance;
    @SerializedName("payment_advance_mode")
    @Expose
    private String paymentAdvanceMode;
    @SerializedName("responce")
    @Expose
    private Boolean responce;

    public Integer getPaymentAdvance() {
        return paymentAdvance;
    }

    public void setPaymentAdvance(Integer paymentAdvance) {
        this.paymentAdvance = paymentAdvance;
    }

    public String getPaymentAdvanceMode() {
        return paymentAdvanceMode;
    }

    public void setPaymentAdvanceMode(String paymentAdvanceMode) {
        this.paymentAdvanceMode = paymentAdvanceMode;
    }

    public Boolean getResponce() {
        return responce;
    }
}
