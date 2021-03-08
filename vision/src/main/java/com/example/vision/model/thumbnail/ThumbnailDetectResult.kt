package com.example.vision.model.thumbnail

import com.example.vision.model.VisionWrapper
import com.google.gson.annotations.SerializedName


data class ThumbnailDetectResult(
    @SerializedName("result")
    val detectedThumbnail: DetectedThumbnail
) : VisionWrapper()