package com.example.vision.model.face

import com.example.vision.model.VisionWrapper
import com.google.gson.annotations.SerializedName


data class FaceDetectResult(
    @SerializedName("result")
    val detectedFace: DetectedFace
) : VisionWrapper()