package me.facuarmo.quicklaunch;

import android.graphics.drawable.Drawable;

class ApplicationInformation {
    String name;
    String packageName;
    Drawable icon;

    ApplicationInformation(String name, String packageName, Drawable icon) {
        this.name = name;
        this.packageName = packageName;
        this.icon = icon;
    }
}