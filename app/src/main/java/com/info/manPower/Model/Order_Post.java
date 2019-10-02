package com.info.manPower.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Order_Post
{
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("sub_cat_id")
    @Expose
    private String subCatId;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("p_cat_id")
    @Expose
    private String pCatId;
    @SerializedName("sub_cat_no")
    @Expose
    private String subCatNo;
    @SerializedName("date_from")
    @Expose
    private String dateFrom;
    @SerializedName("date_to")
    @Expose
    private String dateTo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSubCatId() {
        return subCatId;
    }

    public void setSubCatId(String subCatId) {
        this.subCatId = subCatId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPCatId() {
        return pCatId;
    }

    public void setPCatId(String pCatId) {
        this.pCatId = pCatId;
    }

    public String getSubCatNo() {
        return subCatNo;
    }

    public void setSubCatNo(String subCatNo) {
        this.subCatNo = subCatNo;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }
}
