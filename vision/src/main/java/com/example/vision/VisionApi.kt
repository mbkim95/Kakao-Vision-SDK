package com.example.vision

import com.example.vision.model.OcrResult
import com.example.vision.model.TranslateResult
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface VisionApi {

    @Multipart
    @POST(OCR_PATH)
    fun getOcr(@Part image: MultipartBody.Part): Call<OcrResult>

    @GET(TRANSLATE_PATH)
    fun translateSentence(
        @Query(QUERY) query: String,
        @Query(SOURCE_LANGUAGE) srcLang: String,
        @Query(TARGET_LANGUAGE) targetLang: String
    ): Call<TranslateResult>
}