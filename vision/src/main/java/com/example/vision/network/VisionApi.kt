package com.example.vision.network

import com.example.vision.*
import com.example.vision.model.OcrResult
import com.example.vision.model.ThumbnailCropResult
import com.example.vision.model.ThumbnailDetectResult
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

    @GET(THUMBNAIL_CROP_PATH)
    fun getThumbnailImage(
        @Query(IMAGE_URL) url: String,
        @Query(WIDTH) width: Int,
        @Query(HEIGHT) height: Int
    ): Call<ThumbnailCropResult>

    @Multipart
    @POST(THUMBNAIL_CROP_PATH)
    fun getThumbnailImage(
        @Part image: MultipartBody.Part,
        @Part width: MultipartBody.Part,
        @Part height: MultipartBody.Part,
    ): Call<ThumbnailCropResult>

    @GET(THUMBNAIL_DETECT_PATH)
    fun getDetectedThumbnailImage(
        @Query(IMAGE_URL) url: String,
        @Query(WIDTH) width: Int,
        @Query(HEIGHT) height: Int
    ): Call<ThumbnailDetectResult>

    @Multipart
    @POST(THUMBNAIL_DETECT_PATH)
    fun getDetectedThumbnailImage(
        @Part image: MultipartBody.Part,
        @Part width: MultipartBody.Part,
        @Part height: MultipartBody.Part
    ): Call<ThumbnailDetectResult>
}