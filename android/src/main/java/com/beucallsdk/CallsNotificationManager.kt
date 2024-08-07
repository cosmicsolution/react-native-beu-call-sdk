package com.beucallsdk

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import com.onesignal.notifications.IDisplayableMutableNotification
import com.onesignal.notifications.INotificationReceivedEvent

object CallsNotificationManager {
  private val VIBRATE_PATTERN = longArrayOf(0, 1000, 1000)

  fun initChannels(context: Context) {
    createCallsChannel(context)
  }

  private fun createCallsChannel(context: Context) {
    Log.d("VDEV", "create call channel....")
    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) return

    val manager = context.getSystemService(NotificationManager::class.java)

    val audioAttributes = AudioAttributes.Builder()
      .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
      .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
      .build()

    val channel = NotificationChannel(INCOMING_CALL_CHANNEL_ID, "Calls", NotificationManager.IMPORTANCE_HIGH).apply {
      setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE), audioAttributes)
      vibrationPattern = VIBRATE_PATTERN
      enableLights(true)
      enableVibration(true)
    }

    manager.createNotificationChannel(channel)
  }

  fun displayCall(context: Context, notification: IDisplayableMutableNotification) {
    val packageName = context.applicationContext.packageName
    val focusIntent = context.packageManager.getLaunchIntentForPackage(packageName)!!
      .cloneFilter()
    val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, focusIntent, FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT)

//    val acceptIntent = focusIntent.apply {
//      flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//    }
//    val acceptPendingIntent = PendingIntent.getActivity(context, 0, acceptIntent, FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT)

    val rejectIntent = Intent(context, FakeCallActivity::class.java)
    rejectIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    rejectIntent.putExtra("action", "reject")
    rejectIntent.putExtra("callInfo", notification.additionalData?.toString())

    val rejectPendingIntent = PendingIntent.getActivity(
      context,
      0,
      rejectIntent,
      FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT
    )

    val acceptIntent = Intent(context, FakeCallActivity::class.java).apply {
//      flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
      flags = Intent.FLAG_ACTIVITY_NEW_TASK
      putExtra("action", "accept")
      putExtra("callInfo", notification.additionalData?.toString())
    }
    val acceptPendingIntent = PendingIntent.getActivity(
      context,
      2,
      acceptIntent,
      FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT
    )

//    val rejectIntent =
//      Intent(context, CallRejectReceiver::class.java).apply { action = REJECT_CALL_ACTION }
//    val rejectPendingIntent = PendingIntent.getBroadcast(
//      context,
//      0,
//      rejectIntent,
//      FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT
//    )

    val fullScreenPendingIntent = PendingIntent.getActivity(context, 0, Intent(), FLAG_IMMUTABLE)

    val pattern = longArrayOf(0, 1000, 1000, 1000, 1000)
    val builder: NotificationCompat.Builder = NotificationCompat.Builder(
      context.applicationContext,
      INCOMING_CALL_CHANNEL_ID
    )
      .setSmallIcon(android.R.drawable.sym_call_incoming)
      .setVibrate(pattern)
      .setContentTitle("Incoming call")
      .setContentText("BeU is calling")
      .setDefaults(Notification.DEFAULT_ALL)
      .setPriority(NotificationCompat.PRIORITY_MAX)
      .setCategory(NotificationCompat.CATEGORY_CALL)
      .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
      .setFullScreenIntent(fullScreenPendingIntent, true)
      .setStyle(NotificationCompat.BigTextStyle().bigText("REDiMED is calling"))
      .addAction(android.R.drawable.sym_action_call, "Accept", acceptPendingIntent)
      .addAction(android.R.drawable.ic_delete, "Reject", rejectPendingIntent)
      .setOngoing(true)
      .setAutoCancel(false)
      .setColorized(true)
      .setOnlyAlertOnce(true)

    val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.notify(INCOMING_CALL_NOTIFICATION_ID, builder.build())
  }

  fun dismissCall(context: Context) {
    val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.cancel(INCOMING_CALL_NOTIFICATION_ID)
  }
}
