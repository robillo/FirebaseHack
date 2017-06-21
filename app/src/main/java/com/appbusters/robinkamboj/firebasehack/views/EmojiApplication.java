package com.appbusters.robinkamboj.firebasehack.views;

import android.app.Application;
import android.os.StrictMode;

import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.ios.BuildConfig;
import com.vanniktech.emoji.ios.IosEmojiProvider;

public class EmojiApplication extends Application {
    @Override public void onCreate() {
        super.onCreate();

        EmojiManager.install(new IosEmojiProvider());

        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().build());
        }

    }
}
