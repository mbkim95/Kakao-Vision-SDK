package com.example.vision

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.ResultReceiver
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.vision.model.ImageChooseResult
import java.io.File
import java.io.FileOutputStream

class ImageSelectActivity : AppCompatActivity() {
    private lateinit var resultReceiver: ResultReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_select)

        try {
            intent.extras?.getBundle(KEY_BUNDLE)?.run {
                resultReceiver = getParcelable<ResultReceiver>(IMAGE_SELECTOR) as ResultReceiver
            }
            startActivityForResult(IntentFactory.imageSelect(), IMAGE_SELECT)
        } catch (e: Exception) {
            Log.e("Kakao Vision", "$e")
            sendError(e)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_SELECT && resultCode == RESULT_OK) {
            data?.let {
                val uri = data.data
                if (uri.toString().contains("image")) {
                    uri?.let { uri ->
                        contentResolver.openInputStream(uri).use { inputStream ->
                            val file = File(applicationContext.cacheDir, "image.png")
                            FileOutputStream(file).use {
                                BitmapFactory.decodeStream(inputStream).run {
                                    compress(Bitmap.CompressFormat.PNG, 100, it)
                                }
                            }
                            sendOk(file)
                        }
                    }
                } else {
                    sendError(RuntimeException("image not found"))
                }
            }
        } else {
            sendError(RuntimeException("image not selected"))
        }
        finish()
    }

    private fun sendOk(image: File) {
        resultReceiver.send(RESULT_OK, Bundle().apply {
            putParcelable(IMAGE_CHOOSE_RESULT, ImageChooseResult(image))
        })
    }

    private fun sendError(exception: Exception) {
        if (this::resultReceiver.isInitialized) {
            resultReceiver.send(RESULT_CANCELED, Bundle().apply {
                putSerializable(EXCEPTION, exception)
            })
        }
    }
}