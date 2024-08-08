package com.beucallsdk

import android.app.Activity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import org.json.JSONObject


class FakeCallActivity: Activity() {
  override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onResume() {
    super.onResume()
    Log.d("BeuCallSdk", "FakeCallActivity onResume")
    processCallIntent()
    finish()
  }

  private fun bringAppToForeground() {
    val packageName = applicationContext.packageName
    val focusIntent = packageManager.getLaunchIntentForPackage(packageName)!!
      .cloneFilter()
    startActivity(focusIntent)
  }

  private fun processCallIntent() {
    val intent = intent

    // Extract the extras
    val action = intent.getStringExtra("action")

    Log.d("BeuCallSdk", "Action: $action")
    val callInfoStr = intent.getStringExtra("callInfo")
    callInfoStr?.let {
      val callInfoJson = JSONObject(callInfoStr)
      callInfoJson.put("answer", action)
      try {
        val success = BeuCallSdkModule.instance?.sendNotificationData(callInfoJson) ?: false
        Log.d("BeuCallSdk", "sendNotificationData success: $success sdk null? ${BeuCallSdkModule.instance == null}")
        if (!success) {
          CacheUtils.cache(this, CacheUtils.LAST_NOTIFICATION, callInfoJson.toString())
        }
      } catch (e: Exception) {
        Log.e("BeuCallSdk", "sendNotificationData error: $e")
        CacheUtils.cache(this, CacheUtils.LAST_NOTIFICATION, callInfoJson.toString())
      }
    }

    // Use the extracted variables as needed
    // For example, you could log them
    Log.d("BeuCallSdk", "Action: $action, caller info: $callInfoStr")
    if (action == "accept") {
      bringAppToForeground()
    }
    // TODO: js await will not dismiss notification
    CallsNotificationManager.dismissCall(this)
  }
}
