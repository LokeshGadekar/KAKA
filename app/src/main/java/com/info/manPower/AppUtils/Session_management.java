package com.info.manPower.AppUtils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.info.manPower.View.Login_activity;
import com.info.manPower.View.MainActivity_drawer;

import java.util.HashMap;

import static com.info.manPower.AppUtils.BaseUrl.IS_LOGIN;
import static com.info.manPower.AppUtils.BaseUrl.KEY_DATE;
import static com.info.manPower.AppUtils.BaseUrl.KEY_EMAIL;
import static com.info.manPower.AppUtils.BaseUrl.KEY_HOUSE;
import static com.info.manPower.AppUtils.BaseUrl.KEY_ID;
import static com.info.manPower.AppUtils.BaseUrl.KEY_IMAGE;
import static com.info.manPower.AppUtils.BaseUrl.KEY_MOBILE;
import static com.info.manPower.AppUtils.BaseUrl.KEY_NAME;
import static com.info.manPower.AppUtils.BaseUrl.KEY_PASSWORD;
import static com.info.manPower.AppUtils.BaseUrl.KEY_PINCODE;
import static com.info.manPower.AppUtils.BaseUrl.KEY_SOCITY_ID;
import static com.info.manPower.AppUtils.BaseUrl.KEY_SOCITY_NAME;
import static com.info.manPower.AppUtils.BaseUrl.KEY_TIME;
import static com.info.manPower.AppUtils.BaseUrl.PREFS_NAME;
import static com.info.manPower.AppUtils.BaseUrl.PREFS_NAME2;


public class Session_management {

    SharedPreferences prefs;
    SharedPreferences prefs2;

    SharedPreferences.Editor editor;
    SharedPreferences.Editor editor2;

    public static final String MyPREFERENCES  = "RIGHT_CHOICE";
    public static final String USERNAME = "username";
    public static final String MOBILE_NO = "mobileNo";

    Context context;

    int PRIVATE_MODE = 0;

    public Session_management(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(PREFS_NAME, PRIVATE_MODE);
        editor = prefs.edit();

        prefs2 = context.getSharedPreferences(PREFS_NAME2, PRIVATE_MODE);
        editor2 = prefs2.edit();
    }

    public void createLoginSession(String id, String email, String name
            , String mobile, String image, String pincode, String socity_id,
                                   String socity_name, String house,String password) {

        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_MOBILE, mobile);
        editor.putString(KEY_IMAGE, image);
        editor.putString(KEY_PINCODE, pincode);
        editor.putString(KEY_SOCITY_ID, socity_id);
        editor.putString(KEY_SOCITY_NAME, socity_name);
        editor.putString(KEY_HOUSE, house);
        editor.putString(KEY_PASSWORD,password);
        editor.commit();
    }


    public void createLoginSession(String id, String email,String name,String password ){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_PASSWORD,password);
        editor.commit();

    }

    public void createGuestLogin(String id,String name,String mobileno ){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_MOBILE,mobileno);
        editor.commit();

    }

    public void checkLogin() {

        if (!this.isLoggedIn()) {
            Intent loginsucces = new Intent(context, MainActivity_drawer.class);
            // Closing all the Activities
            loginsucces.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            loginsucces.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(loginsucces);
        }
    }

    /**
     * Get stored session data
     */

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_ID, prefs.getString(KEY_ID, null));
        // user email id
        user.put(KEY_EMAIL, prefs.getString(KEY_EMAIL, null));
        // user name
        user.put(KEY_NAME, prefs.getString(KEY_NAME, null));

        user.put(KEY_MOBILE, prefs.getString(KEY_MOBILE, null));

        user.put(KEY_IMAGE, prefs.getString(KEY_IMAGE, null));

        user.put(KEY_PINCODE, prefs.getString(KEY_PINCODE, null));

        user.put(KEY_SOCITY_ID, prefs.getString(KEY_SOCITY_ID, null));

        user.put(KEY_SOCITY_NAME, prefs.getString(KEY_SOCITY_NAME, null));

        user.put(KEY_HOUSE, prefs.getString(KEY_HOUSE, null));

        user.put(KEY_PASSWORD, prefs.getString(KEY_PASSWORD, null));

        // return user
        return user;
    }

    public void updateData(String name, String mobile, String pincode
            , String socity_id, String image,String house) {

        editor.putString(KEY_NAME, name);
        editor.putString(KEY_MOBILE, mobile);
        editor.putString(KEY_PINCODE, pincode);
        editor.putString(KEY_SOCITY_ID, socity_id);
        editor.putString(KEY_IMAGE, image);
        editor.putString(KEY_HOUSE, house);

        editor.apply();
    }

    public void updateSocity(String socity_name,String socity_id){
        editor.putString(KEY_SOCITY_NAME, socity_name);
        editor.putString(KEY_SOCITY_ID, socity_id);
        editor.apply();
    }

    public void logoutSession() {
        editor.clear();
        editor.commit();

        cleardatetime();

        Intent logout = new Intent(context, MainActivity_drawer.class);
        // Closing all the Activities
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(logout);
    }

    public void logoutSessionwithchangepassword() {
        editor.clear();
        editor.commit();
        cleardatetime();
        Intent logout = new Intent(context, Login_activity.class);
        // Closing all the Activities
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Add new Flag to start new Activity
        logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(logout);
    }

    public void creatdatetime(String date,String time){
        editor2.putString(KEY_DATE, date);
        editor2.putString(KEY_TIME, time);

        editor2.commit();
    }

    public void cleardatetime(){
        editor2.clear();
        editor2.commit();
    }

    public HashMap<String, String> getdatetime() {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_DATE, prefs2.getString(KEY_DATE, null));
        user.put(KEY_TIME, prefs2.getString(KEY_TIME, null));

        return user;
    }



    public static String getUsername(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(MyPREFERENCES, 0);
        return preferences.getString(USERNAME, "");
    }
    public static void setUsername(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences(MyPREFERENCES, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USERNAME, value);
        editor.commit();
    }


    public static String getMobileNo(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(MyPREFERENCES, 0);
        return preferences.getString(MOBILE_NO, "");
    }
    public static void setMobileNo(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences(MyPREFERENCES, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(MOBILE_NO, value);
        editor.commit();
    }


    // Get Login State
    public boolean isLoggedIn() {
        return prefs.getBoolean(IS_LOGIN, false);
    }

}
