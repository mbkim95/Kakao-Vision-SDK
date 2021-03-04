package com.example.vision

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.ResultReceiver
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.vision.IntentFactory.IMAGE_SELECTOR
import com.example.vision.IntentFactory.KEY_BUNDLE
import com.example.vision.model.OcrResult
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class ImageSelectActivity : AppCompatActivity() {
    private lateinit var resultReceiver: ResultReceiver
    private val visionApi = ApiFactory.vapi.create(VisionApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_select)

        try {
            intent.extras?.getBundle(KEY_BUNDLE)?.run {
                resultReceiver = getParcelable<ResultReceiver>(IMAGE_SELECTOR) as ResultReceiver
            }
        } catch (e: Throwable) {
            Log.e("Kakao Vision", "$e")
        }
        startActivityForResult(IntentFactory.imageSelect(), IMAGE_SELECT)
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
                            val image = MultipartBody.Part.createFormData(
                                "image",
                                file.name,
                                file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                            )
                            // 이미지 전송
                            visionApi.getOcr(image).enqueue(object : Callback<OcrResult> {
                                override fun onResponse(
                                    call: retrofit2.Call<OcrResult>,
                                    response: Response<OcrResult>
                                ) {
                                    if (response.isSuccessful) {
                                        response.body()?.let {
                                            sendOk(it)
                                        }
                                    } else {
                                        sendFail()
                                    }
                                }

                                override fun onFailure(
                                    call: retrofit2.Call<OcrResult>,
                                    t: Throwable
                                ) {
                                    resultReceiver.send(RESULT_CANCELED, Bundle().apply {
                                        putSerializable(THROWABLE, t)
                                    })
                                }
                            })
                            finish()
                        }
                    }
                }
            }
        }
    }

    private fun sendOk(data: OcrResult) {
        resultReceiver.send(RESULT_OK, Bundle().apply {
            putParcelable(OCR_RESULT, data)
        })
    }

    private fun sendFail() {
        resultReceiver.send(RESULT_CANCELED, Bundle().apply {
            putSerializable(RUNTIME_EXCEPTION, java.lang.RuntimeException())
        })
    }

    companion object {
        const val IMAGE_SELECT = 1234
        const val OCR_RESULT = "ocr result"
        const val RUNTIME_EXCEPTION = "RuntimeException"
        const val THROWABLE = "throwable"
    }
}