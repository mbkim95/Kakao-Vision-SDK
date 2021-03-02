package com.example.vision

import android.content.Intent
import android.provider.MediaStore

object IntentFactory {
    const val KEY_BUNDLE = "bundle"
    const val IMAGE_SELECTOR = "image selector"

    fun imageSelect(): Intent =
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            type = "image/*"
        }
}