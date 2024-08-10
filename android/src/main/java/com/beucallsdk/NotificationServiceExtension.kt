package com.beucallsdk

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.WindowManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.facebook.react.bridge.ReactMethod
import com.onesignal.notifications.INotificationReceivedEvent
import com.onesignal.notifications.INotificationServiceExtension

class NotificationServiceExtension : INotificationServiceExtension {
  companion object {
    const val TAG = "BeuCallSdk"
  }
  override fun onNotificationReceived(event: INotificationReceivedEvent) {
    Log.d(TAG, "onNotificationReceived ${event.notification.title}")

    // Check the notification type is call?
    // RING: --> display call notification
    // RING_ACCEPTED: --> dismiss call notification -> open react native app with call data
    // RING_REJECTED: --> dismiss call notification -> open react native app with call data
    // HANGUP: --> dismiss call notification
    val additionalData = event.notification.additionalData ?: return

    try {
      val type = additionalData.get("type")
      if (type == "call") {
        event.preventDefault(discard = true)
        val action = additionalData.get("action")

        if (action == "ring") {
          CallsNotificationManager.displayCall(event.context, event.notification)
        } else if (action == "hangup") {
          CallsNotificationManager.dismissCall(context = event.context)
        }
      }
    } catch (e: Exception) {
      Log.e(TAG, "onNotificationReceived error: $e")
    }

  }

//  fun isAppInForeground(context: Context): Boolean {
//    val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
//    val appProcesses = activityManager.runningAppProcesses ?: return false
//
//    for (appProcess in appProcesses) {
//      if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
//        && appProcess.processName == context.packageName) {
//        return true
//      }
//    }
//    return false
//  }

//  @ReactMethod
//  fun backToForeground(context: Context) {
//    if (context == null) {
//      Log.w(TAG, "[RNCallKeepModule][backToForeground] no react context found.")
//      return
//    }
//    val packageName = context.applicationContext.packageName
//    val focusIntent = context.packageManager.getLaunchIntentForPackage(packageName)!!
//      .cloneFilter()

//    val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
//    val appProcesses = activityManager.runningAppProcesses ?: return false

//    val activity: Activity = getCurrentReactActivity()
//    val isOpened = activity != null
//    Log.d(
//      TAG,
//      "[RNCallKeepModule] backToForeground, app isOpened ?" + (if (isOpened) "true" else "false")
//    )

//    if (isAppInForeground(context)) {
//      focusIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
//      context!!.startActivity(focusIntent)
//    } else {
//      focusIntent.addFlags(
//        Intent.FLAG_ACTIVITY_NEW_TASK +
//          WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED +
//          WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD +
//          WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
//      )
//
//      context.startActivity(focusIntent)
//    }
//  }
}
