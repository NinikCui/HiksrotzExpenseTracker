package com.hiksrot.hiksrotzexpensetracker.util

import android.content.Context

object SessionManager {
    private const val PREF_NAME = "user_session"
    private const val KEY_USER_ID = "user_id"

    fun getUserId(context: Context): Int {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return pref.getInt(KEY_USER_ID, -1)
    }

    fun clearSession(context: Context) {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        pref.edit().clear().apply()
    }
}
