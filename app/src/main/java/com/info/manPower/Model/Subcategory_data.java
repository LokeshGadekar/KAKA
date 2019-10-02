package com.info.manPower.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Subcategory_data
{
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("p_cat_id")
    @Expose
    private String pCatId;
    @SerializedName("subcat_name")
    @Expose
    private String subcatName;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("subcat_rate")
    @Expose
    private String subcatRate;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("time_from")
    @Expose
    private String timeFrom;
    @SerializedName("time_to")
    @Expose
    private String timeTo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPCatId() {
        return pCatId;
    }

    public void setPCatId(String pCatId) {
        this.pCatId = pCatId;
    }

    public String getSubcatName() {
        return subcatName;
    }

    public void setSubcatName(String subcatName) {
        this.subcatName = subcatName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubcatRate() {
        return subcatRate;
    }

    public void setSubcatRate(String subcatRate) {
        this.subcatRate = subcatRate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }

    public String getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(String timeTo) {
        this.timeTo = timeTo;
    }


    // ------------------------------------------------------------------

    // Extra fields ................ (Below one are not fetched from API)

    private String hire_from;
    private String hire_to;

    private String num_helpers;

    private String unit_rate;

    public String getUnit_rate() {
        return unit_rate;
    }

    public void setUnit_rate(String unit_rate) {
        this.unit_rate = unit_rate;
    }

    public String getHire_from() {
        return hire_from;
    }

    public void setHire_from(String hire_from) {
        this.hire_from = hire_from;
    }

    public String getNum_helpers() {
        return num_helpers;
    }

    public void setNum_helpers(String num_helpers) {
        this.num_helpers = num_helpers;
    }

    public String getHire_to() {
        return hire_to;
    }

    public void setHire_to(String hire_to) {
        this.hire_to = hire_to;
    }





}

