package com.example.vision

import com.example.vision.model.OcrResult
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface VisionApi {

    @Multipart
    @Headers("Authorization: KakaoAK 168be425c10d5b79ad741b8fefa97c9e",)
    @POST("/v2/vision/text/ocr")
    fun getOcr(@Part image: MultipartBody.Part): Call<OcrResult>
}