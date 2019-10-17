package com.kaka.ics.AppUtils;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPrefrences
{
    public static final String SHARED_PREFERENCE_NAME = "EXPENSEMGT";
    public static final String NAME = "name";
    public static final String MOBILE = "mobile";
    public static final String USERID = "userid";
    public static final String MAIL = "mail";
    public static final String ADDRESS = "address";
    public static final String ORDER_PHONE = "orderphone";
    public static final String LOCATION = "location";
    public static final String USER_TOKEN = "user_token";


    Context context;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor editor2;

    public static final String IS_LOGIN = "isLogin";
    public static final String KEY_NAME = "user_fullname";


    public static String getName(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        return preferences.getString(NAME, "");
    }

    public static void setName(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(NAME, value);
        editor.commit();
    }

    public static String getMail(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        return preferences.getString(MAIL, "");
    }

    public static void setMail(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(MAIL, value);
        editor.commit();
    }

    public static String getAddress(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        return preferences.getString(ADDRESS, "");
    }

    public static void setAddress(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ADDRESS, value);
        editor.commit();
    }

    public static void setMobile(Context context, String headname) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(MOBILE, headname);
        editor.commit();
    }

    public static String getMobile(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        return preferences.getString(MOBILE, "");
    }

    public static void setUserid(Context context, String headname) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USERID, headname);
        editor.commit();
    }

    public static String getUserid(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        return preferences.getString(USERID, "");
    }

    public static String getOrderPhone(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        return preferences.getString(ORDER_PHONE, "");
    }

    public static void setOrderPhone(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ORDER_PHONE, value);
        editor.commit();
    }
    public static String getLocation(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        return preferences.getString(LOCATION, "");
    }

    public static void setLocation(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(LOCATION, value);
        editor.commit();
    }


    public static String getUserToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        return preferences.getString(USER_TOKEN, "");
    }

    public static void setUserToken(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_TOKEN, value);
        editor.commit();
    }

    public void cleardatetime() {
        editor2.clear();
        editor2.commit();
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(IS_LOGIN, false);
    }

}
