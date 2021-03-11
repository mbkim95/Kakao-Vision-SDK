package com.example.vision

import com.example.vision.KakaoVisionSdk.init

/**
 * KakaoVisionSDK 사용에 필요한 설정을 담고 있는 클래스.
 * 이 클래스에서 제공하는 [init] 함수를 사용해 SDK를 사용하기 전에 반드시 초기화 필요.
 *
 *  ```kotlin
 *  class MyApplication : Application {
 *      fun onCreate() {
 *          KakaoVisionSdk.init("${REST_API_KEY}")
 *      }
 *  }
 *  ```
 */
object KakaoVisionSdk {
    internal lateinit var restApiKey: String

    @JvmStatic
    fun init(restApiKey: String) {
        this.restApiKey = restApiKey
    }
}