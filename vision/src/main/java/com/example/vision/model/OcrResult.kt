package com.example.vision.model


import com.google.gson.annotations.SerializedName

data class OcrResult(
    @SerializedName("result")
    val ocrDetailResult: List<OcrDetailResult>
)