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
    private static SharedPreferences mPrefs;
    private static AppPreferencesHelper instance;

    public static AppPreferencesHelper getInstance() {
        if (instance == null) {
            instance = new AppPreferencesHelper(getApplicationContext(),PREF_FILE_NAME);
        }
        return instance;
    }

    public AppPreferencesHelper(Context context, String prefFileName) {
        mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
    }

    public boolean getLoggedInStatus() {
        return mPrefs.getBoolean(PREF_KEY_LOGGED_IN_STATUS, false);
    }

    public void setLoggedInStatus(boolean status) {
        mPrefs.edit().putBoolean(PREF_KEY_LOGGED_IN_STATUS, status).apply();
    }

    public String getCurrentUserName() {
        return mPrefs.getString(PREF_KEY_CURRENT_USER_NAME, null);
    }

    public void setCurrentUserName(String userName) {
        mPrefs.edit().putString(PREF_KEY_CURRENT_USER_NAME, userName).apply();
    }

    public String getCurrentUserId() {
        return mPrefs.getString(PREF_KEY_CURRENT_USER_ID, null);
    }

    public void setCurrentUserId(String userId) {
        mPrefs.edit().putString(PREF_KEY_CURRENT_USER_ID, userId).apply();
    }

    public String getCurrentUserEmail() {
        return mPrefs.getString(PREF_KEY_CURRENT_USER_EMAIL, null);
    }

    public void setCurrentUserEmail(String email) {
        mPrefs.edit().putString(PREF_KEY_CURRENT_USER_EMAIL, email).apply();
    }

    public String getCurrentUserProfilePicUrl() {
        return mPrefs.getString(PREF_KEY_CURRENT_USER_PROFILE_PIC_URL, null);
    }

    public void setCurrentUserProfilePicUrl(String profilePicUrl) {
        mPrefs.edit().putString(PREF_KEY_CURRENT_USER_PROFILE_PIC_URL, profilePicUrl).apply();
    }

    public String getAccessToken() {
        return mPrefs.getString(PREF_KEY_ACCESS_TOKEN, null);
    }

    public void setAccessToken(String accessToken) {
        mPrefs.edit().putString(PREF_KEY_ACCESS_TOKEN, accessToken).apply();
    }

    public String getFcmToken() {
        return mPrefs.getString(PREF_KEY_FCM_TOKEN, null);
    }

    public void setFcmToken(String accessToken) {
        mPrefs.edit().putString(PREF_KEY_FCM_TOKEN, accessToken).apply();
    }
    public String getCurrentUserLoginType() {
        return mPrefs.getString(PREF_KEY_CURRENT_USER_LOGIN_TYPE, null);
    }

    public void setCurrentUserLoginType(String loginType) {
        mPrefs.edit().putString(PREF_KEY_CURRENT_USER_LOGIN_TYPE, loginType).apply();
    }

    public SharedPreferences getmPrefs() {
        return mPrefs;
    }
}
