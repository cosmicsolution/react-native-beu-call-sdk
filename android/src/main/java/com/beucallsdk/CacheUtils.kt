package com.beucallsdk

import android.content.Context

object CacheUtils {
  const val LAST_NOTIFICATION = "LAST_NOTIFICATION"
  const val TAG = "BEU_CALL_SDK_CACHE"
  fun get(context: Context, key: String): String? {
    // get sharepref instance
    val sharedPreferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE)
    return sharedPreferences.getString(key, null)
  }

  fun getAndRemove(context: Context, key: String): String? {
    val sharedPreferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE)
    val value = sharedPreferences.getString(key, null)
    sharedPreferences.edit().remove(key).apply()
    return value
  }

  fun cache(context: Context, key: String, value: String) {
    // get sharepref instance
    val sharedPreferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString(key, value)
    editor.apply()
  }
}
