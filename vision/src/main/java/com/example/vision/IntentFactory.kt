package com.example.vision

import android.content.Intent
import android.provider.MediaStore

object IntentFactory {
    fun imageSelect(): Intent =
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            type = TYPE_IMAGE
        }
}