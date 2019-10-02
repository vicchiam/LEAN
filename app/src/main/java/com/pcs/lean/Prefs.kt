package com.pcs.lean

import android.content.Context
import android.content.SharedPreferences

class Prefs (context: Context) {

    val PREFS_FILENAME = "com.pcs.lean.prefs"
    val SETTINGS_URL = "settings_url"
    val SETTINGS_CENTER = "settings_center"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)

    val defaulURL: String = context.resources.getString(R.string.default_url);

    var settingsPath: String?
        get() = prefs.getString(SETTINGS_URL, defaulURL)
        set(value) = prefs.edit().putString(SETTINGS_URL, value).apply()

    var settingsCenter: Int
        get() = prefs.getInt(SETTINGS_CENTER, 0)
        set(value) = prefs.edit().putInt(SETTINGS_CENTER, value).apply()

}