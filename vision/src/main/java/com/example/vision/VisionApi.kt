package com.example.vision

import com.example.vision.model.OcrResult
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface VisionApi {
    @FormUrlEncoded
    @Headers(
        "Authorization: KakaoAK 168be425c10d5b79ad741b8fefa97c9e",
        "Content-Type: multipart/form-data"
    )
    @POST("/v2/vision/text/ocr")
    fun getOcr(@Field("image") imageUrl: String): Call<OcrResult>
}