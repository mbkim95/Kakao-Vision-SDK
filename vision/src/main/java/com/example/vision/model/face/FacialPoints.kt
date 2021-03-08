package com.example.vision.model.face


import com.google.gson.annotations.SerializedName

data class FacialPoints(
    val jaw: List<List<Double>>,
    @SerializedName("left_eye")
    val leftEye: List<List<Double>>,
    @SerializedName("left_eyebrow")
    val leftEyebrow: List<List<Double>>,
    val lip: List<List<Double>>,
    val nose: List<List<Double>>,
    @SerializedName("right_eye")
    val rightEye: List<List<Double>>,
    @SerializedName("right_eyebrow")
    val rightEyebrow: List<List<Double>>
)