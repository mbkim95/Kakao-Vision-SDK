package com.example.vision.model.thumbnail


import com.example.vision.model.VisionWrapper
import com.google.gson.annotations.SerializedName

data class ThumbnailCropResult(
    @SerializedName("thumbnail_image_url")
    val thumbnailImageUrl: String
): VisionWrapper()