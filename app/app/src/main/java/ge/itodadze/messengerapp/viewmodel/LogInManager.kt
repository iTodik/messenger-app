package ge.itodadze.messengerapp.viewmodel

import android.content.SharedPreferences

class LogInManager(private val sharedPreferences: SharedPreferences) {

    fun isCurrentlyLogged(): Boolean {
        return sharedPreferences.getBoolean(IS_LOGGED, false)
    }

    fun getLoggedUserId(): String? {
        return sharedPreferences.getString(USER_ID, null)
    }

    fun logIn(id: String) {
        sharedPreferences.edit().putBoolean(IS_LOGGED, true)
            .putString(USER_ID, id).apply()
    }

    fun logOut() {
        sharedPreferences.edit().putBoolean(IS_LOGGED, false)
            .putString(USER_ID, null).apply()
    }

    companion object {
        const val FILE_NAME: String = "log_data"
        private const val IS_LOGGED: String = "is_logged"
        private const val USER_ID: String = "user_id"
    }
}