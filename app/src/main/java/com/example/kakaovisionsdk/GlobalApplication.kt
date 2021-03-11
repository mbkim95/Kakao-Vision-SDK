package com.example.kakaovisionsdk

import android.app.Application
import com.example.vision.KakaoVisionSdk
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(applicationContext, NATIVE_APP_KEY, loggingEnabled = true)
        KakaoVisionSdk.init(REST_API_KEY)
    }

    companion object {
        const val NATIVE_APP_KEY = "0a8b260f65463336c10000a6a5d59c79"
        const val REST_API_KEY = "168be425c10d5b79ad741b8fefa97c9e"
    }
}