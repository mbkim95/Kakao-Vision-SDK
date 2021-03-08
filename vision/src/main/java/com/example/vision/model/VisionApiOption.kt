package com.example.vision.model

data class VisionApiOption(
    val api: String,
    val lineBreak: Boolean = false,
    val width: Int = 0,
    val height: Int = 0,
    val threshold: Float? = null
)
