package com.info.manPower.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cancel_order_responce
{
        @SerializedName("responce")
        @Expose
        private Boolean responce;
        @SerializedName("data")
        @Expose
        private String data;

        public Boolean getResponce() {
            return responce;
        }

        public void setResponce(Boolean responce) {
            this.responce = responce;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
}
