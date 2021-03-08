package com.example.vision.extension

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

fun File.convertImageToBinary(name: String, contentType: String): MultipartBody.Part {
    return MultipartBody.Part.createFormData(
        name,
        this.name,
        asRequestBody(contentType.toMediaTypeOrNull())
    )
}
