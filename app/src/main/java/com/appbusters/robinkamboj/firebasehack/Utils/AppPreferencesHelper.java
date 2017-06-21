/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.appbusters.robinkamboj.firebasehack.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by aditya on 27/01/17.
 */

public class AppPreferencesHelper {

    public static final String PREF_FILE_NAME = "My_Pref";
    private static final String PREF_KEY_LOGGED_IN_STATUS = "PREF_KEY_LOGGED_IN_STATUS";
    private static final String PREF_KEY_CURRENT_USER_ID = "PREF_KEY_CURRENT_USER_ID";
    private static final String PREF_KEY_CURRENT_USER_NAME = "PREF_KEY_CURRENT_USER_NAME";
    private static final String PREF_KEY_CURRENT_USER_EMAIL = "PREF_KEY_CURRENT_USER_EMAIL";
    private static final String PREF_KEY_CURRENT_USER_PROFILE_PIC_URL = "PREF_KEY_CURRENT_USER_PROFILE_PIC_URL";
    private static final String PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN";
    private static final String PREF_KEY_FCM_TOKEN = "PREF_KEY_FCM_TOKEN";
    private static final String PREF_KEY_CURRENT_USER_LOGIN_TYPE ="PREF_KEY_CURRENT_USER_LOGIN_TYPE";

    private static final String USER_TYPE = "STUDENT_OR_TEACHER";
    private static final String IS_LOGGED_IN = "true_or_false_login";
    private static final String IS_PROFILE_SET = "true_or_false_profile";
    private static final String IS_INTERESTS_SELECTED = "true_or_false_interest";
    private static final String IS_SUBINTERESTS_SELECTED = "true_or_false_subinterest";

    private static SharedPreferences getPreferences() {
        Context context = getApplicationContext();
        return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public static void setIsLoggedIn(String token){
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(IS_LOGGED_IN, token);
        editor.apply();
    }

    public static void setIsProfileSet(String token){
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(IS_PROFILE_SET, token);
        editor.apply();
    }

    public static String getIsLoggedIn() {
        return getPreferences().getString(IS_LOGGED_IN, null);
    }

    public static String getIsProfileSet() {
        return getPreferences().getString(IS_PROFILE_SET, null);
    }

    public boolean getLoggedInStatus() {
        return getPreferences().getBoolean(PREF_KEY_LOGGED_IN_STATUS, false);
    }

    public void setLoggedInStatus(boolean status) {
        getPreferences().edit().putBoolean(PREF_KEY_LOGGED_IN_STATUS, status).apply();
    }

    public String getCurrentUserName() {
        return getPreferences().getString(PREF_KEY_CURRENT_USER_NAME, null);
    }

    public void setCurrentUserName(String userName) {
        getPreferences().edit().putString(PREF_KEY_CURRENT_USER_NAME, userName).apply();
    }

    public String getCurrentUserId() {
        return getPreferences().getString(PREF_KEY_CURRENT_USER_ID, null);
    }

    public void setCurrentUserId(String userId) {
        getPreferences().edit().putString(PREF_KEY_CURRENT_USER_ID, userId).apply();
    }

    public String getCurrentUserEmail() {
        return getPreferences().getString(PREF_KEY_CURRENT_USER_EMAIL, null);
    }

    public void setCurrentUserEmail(String email) {
        getPreferences().edit().putString(PREF_KEY_CURRENT_USER_EMAIL, email).apply();
    }

    public String getCurrentUserProfilePicUrl() {
        return getPreferences().getString(PREF_KEY_CURRENT_USER_PROFILE_PIC_URL, null);
    }

    public void setCurrentUserProfilePicUrl(String profilePicUrl) {
        getPreferences().edit().putString(PREF_KEY_CURRENT_USER_PROFILE_PIC_URL, profilePicUrl).apply();
    }

    public String getAccessToken() {
        return getPreferences().getString(PREF_KEY_ACCESS_TOKEN, null);
    }

    public void setAccessToken(String accessToken) {
        getPreferences().edit().putString(PREF_KEY_ACCESS_TOKEN, accessToken).apply();
    }

    public String getFcmToken() {
        return getPreferences().getString(PREF_KEY_FCM_TOKEN, null);
    }

    public void setFcmToken(String accessToken) {
        getPreferences().edit().putString(PREF_KEY_FCM_TOKEN, accessToken).apply();
    }
    public String getCurrentUserLoginType() {
        return getPreferences().getString(PREF_KEY_CURRENT_USER_LOGIN_TYPE, null);
    }

    public void setCurrentUserLoginType(String loginType) {
        getPreferences().edit().putString(PREF_KEY_CURRENT_USER_LOGIN_TYPE, loginType).apply();
    }

    public static String getUserType() {
        return getPreferences().getString(USER_TYPE, null);
    }

    public static void setUserType(String userType){
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(USER_TYPE, userType);
        editor.apply();
    }
}
