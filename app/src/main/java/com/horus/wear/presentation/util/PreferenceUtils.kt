package com.horus.wear.presentation.util

import android.content.Context

fun saveUserId(context: Context, userId: String) {
    context.getSharedPreferences("horus", Context.MODE_PRIVATE)
        .edit().putString("userId", userId).apply()
}

fun getSavedUserId(context: Context): String? {
    return context.getSharedPreferences("horus", Context.MODE_PRIVATE)
        .getString("userId", null)
}

fun saveTokens(context: Context, accessToken: String, refreshToken: String) {
    context.getSharedPreferences("horus", Context.MODE_PRIVATE)
        .edit()
        .putString("accessToken", accessToken)
        .putString("refreshToken", refreshToken)
        .apply()
}

fun getAccessToken(context: Context): String? {
    return context.getSharedPreferences("horus", Context.MODE_PRIVATE)
        .getString("accessToken", null)
}

fun getRefreshToken(context: Context): String? {
    return context.getSharedPreferences("horus", Context.MODE_PRIVATE)
        .getString("refreshToken", null)
}

fun saveProfileJson(context: Context, json: String) {
    context.getSharedPreferences("horus", Context.MODE_PRIVATE)
        .edit().putString("profile_json", json).apply()
}

fun getProfileJson(context: Context): String? {
    return context.getSharedPreferences("horus", Context.MODE_PRIVATE)
        .getString("profile_json", null)
}

fun clearSession(context: Context) {
    context.getSharedPreferences("horus", Context.MODE_PRIVATE)
        .edit()
        .remove("userId")
        .remove("accessToken")
        .remove("refreshToken")
        .remove("profile_json")
        .apply()
}

