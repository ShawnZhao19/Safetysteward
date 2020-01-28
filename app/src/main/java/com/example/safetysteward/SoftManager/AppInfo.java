package com.example.safetysteward.SoftManager;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

public class AppInfo {
    private Drawable icon;// 应用图标
    private String appName;// 应用名称
    private String packageName;// 包名

    public AppInfo(Drawable icon, String appName, String packageName) {
        super();
        this.icon = icon;
        this.appName = appName;
        this.packageName = packageName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @NonNull
    @Override
    public String toString() {
        return "AppInfo [icon=" + icon + ", appName=" + appName
                + ", packageName=" + packageName + "]";
    }
}