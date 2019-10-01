package com.pcs.lean

import android.content.Context
import android.content.SharedPreferences

class Prefs (context: Context) {
    val PREFS_FILENAME = "com.pcs.lean.prefs"
    val SETTINGS_URL = "settings_url"
    val SETTINGS_CENTER = "settings_center"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0);

    var settingsPath: String?
        get() = prefs.getString(SETTINGS_URL, "")
        set(value) = prefs.edit().putString(SETTINGS_URL, value).apply()

    var settingsCenter: String?
        get() = prefs.getString(SETTINGS_CENTER, "")
        set(value) = prefs.edit().putString(SETTINGS_CENTER, value).apply()
}