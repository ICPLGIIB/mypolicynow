package com.indicosmic.www.mypolicynow.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.indicosmic.www.mypolicynow.model.Agentinfo;


/**
 * Created by Ind3 on 18-12-17.
 */

//here for this class we are using a singleton pattern

public class SharedPrefManager {

    //the constants
    private static final String SHARED_PREF_NAME = "CLAIM_BIN";

    private static final String KEY_ID = "id";
   // private static final String KEY_IMAGE = "image";
    private static final String KEY_FULLNAME = "full_name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_MOBILENO = "mobile_number";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_DOB = "dob";
    private static final String KEY_IMEI = "imei";
    private static final String KEY_BUSINESS_ID = "business_id";


    private static SharedPrefManager mInstance;
    private static Context mCtx;

    public SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //method to let the user login
    //this method will store the user data in shared preferences
    public void agentLogin(Agentinfo agentinfo) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_ID,agentinfo.getId());
        editor.putString(KEY_FULLNAME,agentinfo.getFull_name());
        editor.putString(KEY_EMAIL,agentinfo.getEmail());
        editor.putString(KEY_MOBILENO,agentinfo.getMobile_number());
        editor.putString(KEY_ADDRESS,agentinfo.getAddress());
        editor.putString(KEY_DOB,agentinfo.getDob());
        editor.putString(KEY_IMEI,agentinfo.getImei());
        editor.putString(KEY_BUSINESS_ID,agentinfo.getBusiness_id());
        editor.apply();
    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_MOBILENO, null) != null;
    }

    //this method will give the logged in user
    public Agentinfo getAgent() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new Agentinfo(

                sharedPreferences.getString(KEY_ID, null),
                sharedPreferences.getString(KEY_FULLNAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_MOBILENO, null),
                sharedPreferences.getString(KEY_ADDRESS, null),
                sharedPreferences.getString(KEY_DOB, null),
                sharedPreferences.getString(KEY_IMEI,null),
                sharedPreferences.getString(KEY_BUSINESS_ID,null)
        );
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }


}