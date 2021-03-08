package com.example.vision.model.face


data class Result(
    val faces: List<Face>,
    val height: Int,
    val width: Int
)