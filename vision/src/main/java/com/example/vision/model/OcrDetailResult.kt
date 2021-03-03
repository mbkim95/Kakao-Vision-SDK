package com.example.vision.model


import com.google.gson.annotations.SerializedName

data class OcrDetailResult(
    @SerializedName("boxes")
    val boxes: List<List<Int>>,
    @SerializedName("recognition_words")
    val recognitionWords: List<String>
)