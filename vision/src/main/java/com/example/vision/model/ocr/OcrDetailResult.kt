package com.example.vision.model.ocr


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class OcrDetailResult(
    @SerializedName("boxes")
    val boxes: List<List<Int>>,
    @SerializedName("recognition_words")
    val recognitionWords: List<String>
) : Parcelable