package com.example.kakaovisionsdk

import android.app.Application
import com.example.vision.KakaoVisionSdk

class GlobalApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoVisionSdk.init(applicationContext, REST_API_KEY)
    }

    companion object{
         const val REST_API_KEY = "168be425c10d5b79ad741b8fefa97c9e"
    }
}