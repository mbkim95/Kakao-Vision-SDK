package com.example.vision

import com.example.vision.model.OcrResult
import com.example.vision.model.TranslateResult
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface VisionApi {

    @Multipart
    @Headers("Authorization: KakaoAK 168be425c10d5b79ad741b8fefa97c9e")
    @POST("/v2/vision/text/ocr")
    fun getOcr(@Part image: MultipartBody.Part): Call<OcrResult>

    @GET("/v2/translation/translate")
    @Headers("Authorization: KakaoAK 168be425c10d5b79ad741b8fefa97c9e")
    fun translateSentence(
        @Query("query", encoded = true) query: String,
        @Query("src_lang") srcLang: String,
        @Query("target_lang") targetLang: String
    ): Call<TranslateResult>
}