package com.pcs.lean

import android.content.Context
import android.content.SharedPreferences

class Prefs (context: Context) {

    private val PREFS_FILENAME = "com.pcs.lean.prefs"
    private val SETTINGS_URL = "settings_url"
    private val SETTINGS_CENTER = "settings_center"
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)

    private val defaulURL: String = context.resources.getString(R.string.default_url)

    var settingsPath: String?
        get() = prefs.getString(SETTINGS_URL, defaulURL)
        set(value) = prefs.edit().putString(SETTINGS_URL, value).apply()

    var settingsCenter: Int
        get() = prefs.getInt(SETTINGS_CENTER, 0)
        set(value) = prefs.edit().putInt(SETTINGS_CENTER, value).apply()

}