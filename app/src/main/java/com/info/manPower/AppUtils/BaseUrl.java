package com.info.manPower.AppUtils;

import com.info.manPower.API_retro.API_parameter;
import com.info.manPower.API_retro.Retrofit_client;

public class BaseUrl
{
    public static String baseimg = "https://www.lotusenterprises.net/manpower/";
    public static String base = "https://www.lotusenterprises.net/manpower/Api/";

    public static String registration = "https://www.lotusenterprises.net/manpower/Api/registration";
    public static String category = "https://www.lotusenterprises.net/manpower/Api/get_category";

    public static API_parameter getAPIService(){
        return Retrofit_client.getClient(base).create(API_parameter.class);
    }
    public static final String PREFS_NAME = "ManPowerLoginPrefs";

    public static final String PREFS_NAME2 = "ManPowerLoginPrefs2";

    public static final String IS_LOGIN = "isLogin";

    public static final String KEY_NAME = "user_fullname";

    public static final String KEY_EMAIL = "user_email";

    public static final String KEY_ID = "user_id";

    public static final String KEY_MOBILE = "user_phone";

    public static final String KEY_IMAGE = "user_image";

    public static final String KEY_PINCODE = "pincode";

    public static final String KEY_SOCITY_ID = "Socity_id";

    public static final String KEY_SOCITY_NAME = "socity_name";

    public static final String KEY_HOUSE = "house_no";

    public static final String KEY_DATE = "date";

    public static final String KEY_TIME = "time";

    public static final String KEY_PASSWORD = "password";
}
