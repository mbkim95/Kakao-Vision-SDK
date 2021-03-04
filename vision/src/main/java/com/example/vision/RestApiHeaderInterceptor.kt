package com.example.vision

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class RestApiHeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().let {
            val header = KAKAO_AK.format(KakaoVisionSdk.restApiKey)
            Log.d("Kakao Vision", "intercept: $header")
            it.newBuilder().addHeader(AUTH_HEADER, header).build()
        }
        return chain.proceed(request)
    }
}