package com.example.vision.model


import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("height")
    val height: Int,
    @SerializedName("thumbnail")
    val thumbnail: Thumbnail,
    @SerializedName("width")
    val width: Int
)