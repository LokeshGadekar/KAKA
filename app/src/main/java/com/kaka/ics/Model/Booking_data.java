package com.kaka.ics.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Booking_data
{
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("advance")
    @Expose
    private String advance;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("payment_mode")
    @Expose
    private String paymentMode;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("total_amt")
    @Expose
    private String totalAmt;
    @SerializedName("cat_name")
    @Expose
    private String catName;
    @SerializedName("order_cancel")
    @Expose
    private String order_Cancel;
    @SerializedName("payment_complete")
    @Expose
    private String payment_complete;
    @SerializedName("cancel_time")
    @Expose
    private String cancel_time;

    public String getPayment_complete() {
        return payment_complete;
    }

    public void setPayment_complete(String payment_complete) {
        this.payment_complete = payment_complete;
    }


    public String getCancel_time() {
        return cancel_time;
    }

    public void setCancel_time(String cancel_time) {
        this.cancel_time = cancel_time;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAdvance() {
        return advance;
    }

    public void setAdvance(String advance) {
        this.advance = advance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(String totalAmt) {
        this.totalAmt = totalAmt;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;}

    public String getOrder_Cancel() {
        return order_Cancel;
    }

    public void setOrder_Cancel(String order_Cancel) {
        this.order_Cancel = order_Cancel;
    }
}
