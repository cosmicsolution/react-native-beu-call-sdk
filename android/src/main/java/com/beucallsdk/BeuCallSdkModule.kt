package com.beucallsdk

import android.util.Log
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.WritableMap
import com.facebook.react.modules.core.DeviceEventManagerModule
import org.json.JSONObject

class BeuCallSdkModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {
  private lateinit var reactContext: ReactApplicationContext

  init {
        CallsNotificationManager.initChannels(context = reactContext.applicationContext)
    }

  override fun getName(): String {
    return NAME
  }

  private fun sendEvent(eventName: String, params: WritableMap?) {
    Log.d("VDEV", "sendEvent $eventName data: $params")
    reactContext
      .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
      .emit(eventName, params)
  }

  fun sendNotificationData(info: JSONObject) {
    sendEvent("IncomingCallInfoReceived", JSONConverter.convertJsonToMap(info))
  }

  private var listenerCount = 0
  // Example method
  // See https://reactnative.dev/docs/native-modules-android
  @ReactMethod
  fun multiply(a: Double, b: Double, promise: Promise) {
    Log.d("BeuCallSdk", "multiply called")
    sendEvent("IncomingCallInfoReceived", Arguments.createMap().apply {
      putString("test", "test")
    })
    promise.resolve(a * b)
  }

  @ReactMethod
  fun addListener(eventName: String) {
    if (listenerCount == 0) {
      // Set up any upstream listeners or background tasks as necessary
    }

    listenerCount += 1
  }

  @ReactMethod
  fun removeListeners(count: Int) {
    listenerCount -= count
    if (listenerCount == 0) {
      // Remove upstream listeners, stop unnecessary background tasks
    }
  }

  companion object {
    var instance: BeuCallSdkModule? = null
      private set
    const val TAG = "VDEV"

    fun getInstance(reactContext: ReactApplicationContext, realContext: Boolean = false): NativeModule {
      if (instance == null) {
        Log.d(TAG, "[ getInstance : " + (if (reactContext == null) "null" else "ok"))
        instance = BeuCallSdkModule(reactContext)
//        instance.registerReceiver()
//        fetchStoredSettings(reactContext)
      }

      if (realContext) {
        instance!!.setContext(reactContext)
      }

      return instance!!
    }

    const val NAME = "BeuCallSdk"
  }

  private fun setContext(reactContext: ReactApplicationContext) {
    Log.d(TAG, "[RNCallKeepModule] updating react context");
    this.reactContext = reactContext;
  }
}
