package com.kaushalya.app.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Manages user login session using SharedPreferences.
 */
class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "KaushalyaSession"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_USER_ID = "userId"
        private const val KEY_USER_NAME = "userName"
        private const val KEY_USER_EMAIL = "userEmail"
        private const val KEY_USER_TYPE = "userType"
        private const val KEY_WORKER_ID = "workerId"
    }

    fun saveLoginSession(userId: Int, userName: String, email: String, userType: String) {
        prefs.edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, true)
            putInt(KEY_USER_ID, userId)
            putString(KEY_USER_NAME, userName)
            putString(KEY_USER_EMAIL, email)
            putString(KEY_USER_TYPE, userType)
            apply()
        }
    }

    fun saveWorkerId(workerId: Int) {
        prefs.edit().putInt(KEY_WORKER_ID, workerId).apply()
    }

    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED_IN, false)

    fun getUserId(): Int = prefs.getInt(KEY_USER_ID, -1)

    fun getUserName(): String = prefs.getString(KEY_USER_NAME, "") ?: ""

    fun getUserEmail(): String = prefs.getString(KEY_USER_EMAIL, "") ?: ""

    fun getUserType(): String = prefs.getString(KEY_USER_TYPE, "Customer") ?: "Customer"

    fun getWorkerId(): Int = prefs.getInt(KEY_WORKER_ID, -1)

    fun isWorker(): Boolean = getUserType() == "Worker"

    fun logout() {
        prefs.edit().clear().apply()
    }
}
