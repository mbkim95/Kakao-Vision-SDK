package com.example.vision.model.face


import com.google.gson.annotations.SerializedName

data class Face(
    @SerializedName("class_idx")
    val classIdx: Int,
    @SerializedName("facial_attributes")
    val facialAttributes: FacialAttributes,
    @SerializedName("facial_points")
    val facialPoints: FacialPoints,
    val h: Double,
    val pitch: Double,
    val roll: Double,
    val score: Double,
    val w: Double,
    val x: Double,
    val y: Double,
    val yaw: Double
)