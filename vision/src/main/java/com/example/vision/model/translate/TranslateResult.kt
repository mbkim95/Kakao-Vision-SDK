package com.example.vision.model.translate


import com.example.vision.model.VisionWrapper
import com.google.gson.annotations.SerializedName

data class TranslateResult(
    @SerializedName("translated_text")
    val translatedText: List<List<String>>
): VisionWrapper()