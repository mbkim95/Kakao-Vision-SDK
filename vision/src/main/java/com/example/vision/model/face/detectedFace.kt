package com.example.vision.model.face


data class DetectedFace(
    val faces: List<Face>,
    val height: Int,
    val width: Int
)