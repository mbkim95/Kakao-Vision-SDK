package com.example.vision.network

import com.example.vision.AUTH_HEADER
import com.example.vision.KAKAO_AK
import com.example.vision.KakaoVisionSdk
import okhttp3.Interceptor
import okhttp3.Response

class RestApiHeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().let {
            val header = KAKAO_AK.format(KakaoVisionSdk.restApiKey)
            it.newBuilder().addHeader(AUTH_HEADER, header).build()
        }
        return chain.proceed(request)
    }
}