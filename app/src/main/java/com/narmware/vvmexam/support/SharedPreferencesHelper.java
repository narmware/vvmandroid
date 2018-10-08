package com.narmware.vvmexam.support;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by comp16 on 12/19/2017.
 */

public class SharedPreferencesHelper {

    private static final String IS_LOGIN="login";
    private static final String IS_TC_ACCPTED="termsConditions";
    private static final String USER_STATE="state";
    private static final String USER_DISTRICT="district";
    private static final String USER_CITY="city";
    private static final String PREFF_EXAM_STATE="exam_state";
    private static final String PREFF_EXAM_CITY="exam_city";
    private static final String USER_NAME="name";
    private static final String USER_GENDER="gender";
    private static final String USER_MOBILE="mobile";
    private static final String USER_PASSWORD="password";
    private static final String USER_PROFILE_IMAGE="profile_image";

    private static final String MINIMIZE_COUNT="minimize_count";

    public static void setIsLogin(boolean login, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putBoolean(IS_LOGIN,login);
        edit.commit();
    }

    public static boolean getIsLogin(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        boolean login=pref.getBoolean(IS_LOGIN,false);
        return login;
    }

    public static void setIsTcAccpted(boolean termsConditions, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putBoolean(IS_TC_ACCPTED,termsConditions);
        edit.commit();
    }

    public static boolean getIsTcAccpted(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        boolean termsConditions=pref.getBoolean(IS_TC_ACCPTED,false);
        return termsConditions;
    }

    public static void setUserState(String state, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(USER_STATE,state);
        edit.commit();
    }

    public static String getUserState(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String state=pref.getString(USER_STATE,null);
        return state;
    }

    public static void setUserDistrict(String dist, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(USER_DISTRICT,dist);
        edit.commit();
    }

    public static String getUserDistrict(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String dist=pref.getString(USER_DISTRICT,null);
        return dist;
    }

    public static void setUserCity(String city, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(USER_CITY,city);
        edit.commit();
    }

    public static String getUserCity(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String city=pref.getString(USER_CITY,null);
        return city;
    }

    public static void setUserName(String name, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(USER_NAME,name);
        edit.commit();
    }

    public static String getUserName(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String name=pref.getString(USER_NAME,null);
        return name;
    }


    public static void setUserGender(String str, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(USER_GENDER,str);
        edit.commit();
    }

    public static String getUserGender(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String str=pref.getString(USER_GENDER,null);
        return str;
    }


    public static void setUserMobile(String str, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(USER_MOBILE,str);
        edit.commit();
    }

    public static String getUserMobile(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String str=pref.getString(USER_MOBILE,null);
        return str;
    }

    public static void setUserPassword(String str, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(USER_PASSWORD,str);
        edit.commit();
    }

    public static String getUserPassword(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String str=pref.getString(USER_PASSWORD,null);
        return str;
    }

    public static void setPreffExamState(String str, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(PREFF_EXAM_STATE,str);
        edit.commit();
    }

    public static String getPreffExamState(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String str=pref.getString(PREFF_EXAM_STATE,null);
        return str;
    }

    public static void setPreffExamCity(String str, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(PREFF_EXAM_CITY,str);
        edit.commit();
    }

    public static String getPreffExamCity(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String str=pref.getString(PREFF_EXAM_CITY,null);
        return str;
    }


    public static void setMinimizeCount(int count, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putInt(MINIMIZE_COUNT,count);
        edit.commit();
    }

    public static int getMinimizeCount(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        int count=pref.getInt(MINIMIZE_COUNT,0);
        return count;
    }
    public static void setUserProfileImage(String prof_img, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putString(USER_PROFILE_IMAGE,prof_img);
        edit.commit();
    }

    public static String getUserProfileImage(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        String prof_img=pref.getString(USER_PROFILE_IMAGE,null);
        return prof_img;
    }



}
