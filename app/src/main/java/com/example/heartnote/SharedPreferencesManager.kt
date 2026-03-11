package com.example.heartnote

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager(context: Context) {
    // Shared Preferences File
    private val preferences: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_ID = "user_id"

        private const val KEY_USERNAME = "username"
    }

    // Login: บันทึกสถานะการเข้าสู่ระบบและข้อมูลพื้นฐาน
    fun saveLoginStatus(isLoggedIn: Boolean, userId: Int, username: String) {
        val editor = preferences.edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
        editor.putInt(KEY_USER_ID, userId)
        editor.putString(KEY_USERNAME, username)
        editor.apply() // Save data
    }

    // get std_ID: ดึงค่า ID ที่บันทึกไว้
    fun getSavedUserId(): Int {
        return preferences.getInt(KEY_USER_ID, -1)
    }

    fun getSavedUsername(): String? {
        return preferences.getString(KEY_USERNAME, null)
    }

    // Logout: ล้างข้อมูลเมื่อออกจากระบบ
    fun logout(rememberId: Boolean) {
        val editor = preferences.edit()
        editor.remove(KEY_IS_LOGGED_IN)

        // ถ้าไม่เลือกจำ ID (Remember Me) ก็ให้ลบ ID ออกด้วย
        if (!rememberId) {
            editor.remove(KEY_USER_ID)
            editor.remove(KEY_USERNAME)
        }
        editor.apply()
    }
}