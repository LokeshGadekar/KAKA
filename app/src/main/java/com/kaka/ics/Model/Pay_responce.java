package com.kaka.ics.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pay_responce
{
    @SerializedName("payment_advance")
    @Expose
    int payment_advance;

    @SerializedName("payment_complete")
    @Expose
    int payment_complete;

    @SerializedName("payment_mode")
    @Expose
    String payment_mode;

    @SerializedName("payment_advance_mode")
    @Expose
    String payment_advance_mode;

    @SerializedName("responce")
    @Expose
    Boolean responce;

    public int getPayment_advance() {
        return payment_advance;
    }

    public void setPayment_advance(int payment_advance) {
        this.payment_advance = payment_advance;
    }

    public int getPayment_complete() {
        return payment_complete;
    }

    public void setPayment_complete(int payment_complete) {
        this.payment_complete = payment_complete;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    public String getPayment_advance_mode() {
        return payment_advance_mode;
    }

    public void setPayment_advance_mode(String payment_advance_mode) {
        this.payment_advance_mode = payment_advance_mode;
    }

    public Boolean getResponce() {
        return responce;
    }

    public void setResponce(Boolean responce) {
        this.responce = responce;
    }
}
