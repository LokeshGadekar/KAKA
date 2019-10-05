package com.info.manPower.API_retro;

import com.info.manPower.Model.Booking_Responce;
import com.info.manPower.Model.Cart_Responce;
import com.info.manPower.Model.Enquiry_Responce;
import com.info.manPower.Model.Order_Post;
import com.info.manPower.Model.Profile_responce;
import com.info.manPower.Model.Registration_Responce;
import com.info.manPower.Model.Order_Responce;
import com.info.manPower.Model.Slider_Responce;
import com.info.manPower.Model.Subcategory_Responce;
import com.info.manPower.Model.Suborder_Responce;
import com.info.manPower.Model.Support_Responce;
import com.info.manPower.Model.UpdateProfile_Responce;

import org.json.JSONArray;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface API_parameter
{
    @FormUrlEncoded
    @POST("registration")
    Call<Registration_Responce> REGISTRATION_CALL(
            @Field("f_name") String name,
            @Field("l_name") String lname,
            @Field("user_email") String mail,
            @Field("mobile_no") String mobile,
            @Field("password") String password,
            @Field("address") String address,
            @Field("gender") String gender
    );

    @FormUrlEncoded
    @POST("login")
    Call<Registration_Responce> LOGIN_CALL(
            @Field("mobile_no") String mobile,
            @Field("password") String pass
    );

    @GET("get_support")
    Call<Support_Responce> SUPPORT_CALL();

    @GET("get_about_us")
    Call<Support_Responce> ABOUT_CALL();

    @GET("get_terms")
    Call<Support_Responce> TERMS_CALL();

    @GET("get_slider")
    Call<Slider_Responce> SLIDER_CALL();

    @FormUrlEncoded
    @POST("get_subcategory")
    Call<Subcategory_Responce> SubCategory_CALL(
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("pending_approval")
    Call<Order_Responce> Order_Cart(
            @Field("data")JSONArray jsonArray
            );

    @FormUrlEncoded
    @POST("get_cart")
    Call<Cart_Responce> CART_CALL(
            @Field("user_id")String userid
    );

    @FormUrlEncoded
    @POST("get_profile")
    Call<Profile_responce> PROFILE_CALL(
       @Field("user_id")String userid
    );

    @FormUrlEncoded
    @POST("update_profile")
    Call<UpdateProfile_Responce> UPDATE_PROFILE_CALL(
            @Field("user_id")String userid,
            @Field("f_name")String name,
            @Field("l_name")String lname,
            @Field("address")String address
    );

    @FormUrlEncoded
    @POST("get_order_cat")
    Call<Booking_Responce> BOOKING_CALL(
        @Field("user_id")String userid
    );

    @FormUrlEncoded
    @POST("get_order")
    Call<Suborder_Responce> SUBORDER_CALL(
            @Field("user_id")String userid,
            @Field("order_id")String orderid
    );

    @FormUrlEncoded
    @POST("contractual_worker")
    Call<Enquiry_Responce> ENQUIRY_CALL(
      @Field("name")String name,
      @Field("mobile_no")String mobile,
      @Field("address_one")String address1,
      @Field("address_two")String address2,
      @Field("worker_no")String wnum,
      @Field("description")String wdesc,
      @Field("worker_cat")String catid,
      @Field("cat_name")String ctname,
      @Field("e_d_h")String date
    );

}
