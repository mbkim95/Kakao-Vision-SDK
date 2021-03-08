package com.example.vision.model.ocr


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class OcrResult(
    @SerializedName("result")
    val ocrDetailResult: List<OcrDetailResult>
) : Parcelable