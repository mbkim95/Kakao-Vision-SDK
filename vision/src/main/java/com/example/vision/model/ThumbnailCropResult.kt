package com.example.vision.model


import com.google.gson.annotations.SerializedName

data class ThumbnailCropResult(
    @SerializedName("thumbnail_image_url")
    val thumbnailImageUrl: String
)