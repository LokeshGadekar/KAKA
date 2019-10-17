package com.kaka.ics.AppUtils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.kaka.ics.View.Login_activity;
import com.kaka.ics.View.MainActivity_drawer;

import java.util.HashMap;


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
        prefs = context.getSharedPreferences(BaseUrl.PREFS_NAME, PRIVATE_MODE);
        editor = prefs.edit();

        prefs2 = context.getSharedPreferences(BaseUrl.PREFS_NAME2, PRIVATE_MODE);
        editor2 = prefs2.edit();
    }

    public void createLoginSession(String id, String email, String name
            , String mobile, String image, String pincode, String socity_id,
                                   String socity_name, String house,String password) {

        editor.putBoolean(BaseUrl.IS_LOGIN, true);
        editor.putString(BaseUrl.KEY_ID, id);
        editor.putString(BaseUrl.KEY_EMAIL, email);
        editor.putString(BaseUrl.KEY_NAME, name);
        editor.putString(BaseUrl.KEY_MOBILE, mobile);
        editor.putString(BaseUrl.KEY_IMAGE, image);
        editor.putString(BaseUrl.KEY_PINCODE, pincode);
        editor.putString(BaseUrl.KEY_SOCITY_ID, socity_id);
        editor.putString(BaseUrl.KEY_SOCITY_NAME, socity_name);
        editor.putString(BaseUrl.KEY_HOUSE, house);
        editor.putString(BaseUrl.KEY_PASSWORD,password);
        editor.commit();
    }


    public void createLoginSession(String id, String email,String name,String password ){
        editor.putBoolean(BaseUrl.IS_LOGIN, true);
        editor.putString(BaseUrl.KEY_ID, id);
        editor.putString(BaseUrl.KEY_EMAIL, email);
        editor.putString(BaseUrl.KEY_NAME, name);
        editor.putString(BaseUrl.KEY_PASSWORD,password);
        editor.commit();

    }

    public void createGuestLogin(String id,String name,String mobileno ){
        editor.putBoolean(BaseUrl.IS_LOGIN, true);
        editor.putString(BaseUrl.KEY_ID, id);
        editor.putString(BaseUrl.KEY_NAME, name);
        editor.putString(BaseUrl.KEY_MOBILE,mobileno);
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

        user.put(BaseUrl.KEY_ID, prefs.getString(BaseUrl.KEY_ID, null));
        // user email id
        user.put(BaseUrl.KEY_EMAIL, prefs.getString(BaseUrl.KEY_EMAIL, null));
        // user name
        user.put(BaseUrl.KEY_NAME, prefs.getString(BaseUrl.KEY_NAME, null));

        user.put(BaseUrl.KEY_MOBILE, prefs.getString(BaseUrl.KEY_MOBILE, null));

        user.put(BaseUrl.KEY_IMAGE, prefs.getString(BaseUrl.KEY_IMAGE, null));

        user.put(BaseUrl.KEY_PINCODE, prefs.getString(BaseUrl.KEY_PINCODE, null));

        user.put(BaseUrl.KEY_SOCITY_ID, prefs.getString(BaseUrl.KEY_SOCITY_ID, null));

        user.put(BaseUrl.KEY_SOCITY_NAME, prefs.getString(BaseUrl.KEY_SOCITY_NAME, null));

        user.put(BaseUrl.KEY_HOUSE, prefs.getString(BaseUrl.KEY_HOUSE, null));

        user.put(BaseUrl.KEY_PASSWORD, prefs.getString(BaseUrl.KEY_PASSWORD, null));

        // return user
        return user;
    }

    public void updateData(String name, String mobile, String pincode
            , String socity_id, String image,String house) {

        editor.putString(BaseUrl.KEY_NAME, name);
        editor.putString(BaseUrl.KEY_MOBILE, mobile);
        editor.putString(BaseUrl.KEY_PINCODE, pincode);
        editor.putString(BaseUrl.KEY_SOCITY_ID, socity_id);
        editor.putString(BaseUrl.KEY_IMAGE, image);
        editor.putString(BaseUrl.KEY_HOUSE, house);

        editor.apply();
    }

    public void updateSocity(String socity_name,String socity_id){
        editor.putString(BaseUrl.KEY_SOCITY_NAME, socity_name);
        editor.putString(BaseUrl.KEY_SOCITY_ID, socity_id);
        editor.apply();
    }

    public void logoutSession() {
        editor.clear();
        editor.commit();
        editor.putBoolean(BaseUrl.IS_LOGIN ,false);
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
        editor2.putString(BaseUrl.KEY_DATE, date);
        editor2.putString(BaseUrl.KEY_TIME, time);

        editor2.commit();
    }

    public void cleardatetime(){
        editor2.clear();
        editor2.commit();
    }

    public HashMap<String, String> getdatetime() {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(BaseUrl.KEY_DATE, prefs2.getString(BaseUrl.KEY_DATE, null));
        user.put(BaseUrl.KEY_TIME, prefs2.getString(BaseUrl.KEY_TIME, null));

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
        return prefs.getBoolean(BaseUrl.IS_LOGIN, false);
    }

}
