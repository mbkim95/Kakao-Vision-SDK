package com.example.vision.model


import com.google.gson.annotations.SerializedName

data class TranslateResult(
    @SerializedName("translated_text")
    val translatedText: List<List<String>>
)