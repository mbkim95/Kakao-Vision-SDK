package com.example.vision

import android.content.Context
import android.content.SharedPreferences
import com.example.vision.KakaoVisionSdk.init

/**
 * KakaoVisionSDK 사용에 필요한 설정을 담고 있는 클래스.
 * 이 클래스에서 제공하는 [init] 함수를 사용해 SDK를 사용하기 전에 반드시 초기화 필요.
 *
 *  ```kotlin
 *  class MyApplication : Application {
 *      fun onCreate() {
 *          KakaoSdk.init(this, "${NATIVE_APP_KEY}")
 *      }
 *  }
 *  ```
 */
object KakaoVisionSdk {
    private lateinit var sharedPreferences: SharedPreferences
    internal val restApiKey: String get() = sharedPreferences.getString(REST_API_KEY, "").toString()

    @JvmStatic
    fun init(context: Context, restApiKey: String) {
        sharedPreferences =
            context.getSharedPreferences(SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(REST_API_KEY, restApiKey).apply()
    }
}