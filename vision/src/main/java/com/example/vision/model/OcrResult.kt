package com.example.vision.model

data class OcrResult(val boxes: List<List<Point>>, val recognitionWords: List<String>)
