package com.beucallsdk

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat

class CallRejectReceiver : BroadcastReceiver() {
  override fun onReceive(context: Context, intent: Intent) {
    if (intent.action == REJECT_CALL_ACTION) {
      Log.d("BeuCallSdk", "reject call...")
      val notificationManager = NotificationManagerCompat.from(context)
      notificationManager.cancel(INCOMING_CALL_NOTIFICATION_ID)
    }
  }
}
