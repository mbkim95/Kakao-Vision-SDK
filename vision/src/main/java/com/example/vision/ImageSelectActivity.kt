package com.example.vision

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.ResultReceiver
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.vision.VisionApiClient.Companion.API_CODE
import com.example.vision.VisionApiClient.Companion.OCR
import com.example.vision.VisionApiClient.Companion.THUMBNAIL
import com.example.vision.model.OcrResult
import com.example.vision.model.ThumbnailCropResult
import com.example.vision.network.ApiFactory
import com.example.vision.network.VisionApi
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

// TODO: Activity는 이미지 선택해서 VisionApiClient에게 이미지를 바이너리로 전달하고 VisionApiClient에서 API 호출하는 구조로 수정
class ImageSelectActivity : AppCompatActivity() {
    private lateinit var resultReceiver: ResultReceiver
    private var cropWidth: Int = 0
    private var cropHeight: Int = 0
    private lateinit var apiCode: String
    private val visionApi = ApiFactory.vapi.create(VisionApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_select)

        try {
            intent.extras?.getBundle(KEY_BUNDLE)?.run {
                resultReceiver = getParcelable<ResultReceiver>(IMAGE_SELECTOR) as ResultReceiver
                apiCode = getString(API_CODE, "")
                cropWidth = getInt(WIDTH, 0)
                cropHeight = getInt(HEIGHT, 0)
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

                            val image = convertImageToBinary(file)

                            if (apiCode == OCR) {
                                callOcrApi(image)
                            } else if (apiCode == THUMBNAIL) {
                                callThumbnailCropApi(image, cropWidth, cropHeight)
                            }
                        }
                    }
                }
            }
        }
        finish()
    }

    private fun callOcrApi(image: MultipartBody.Part) {
        visionApi.getOcr(image).enqueue(object : Callback<OcrResult> {
            override fun onResponse(
                call: Call<OcrResult>,
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
                call: Call<OcrResult>,
                t: Throwable
            ) {
                resultReceiver.send(RESULT_CANCELED, Bundle().apply {
                    putSerializable(THROWABLE, t)
                })
            }
        })
    }

    private fun callThumbnailCropApi(image: MultipartBody.Part, width: Int, height: Int) {
        val w = MultipartBody.Part.createFormData(WIDTH, width.toString())
        val h = MultipartBody.Part.createFormData(HEIGHT, height.toString())

        // 이미지 전송
        visionApi.getThumbnailImage(image, w, h).enqueue(object : Callback<ThumbnailCropResult> {
            override fun onResponse(
                call: Call<ThumbnailCropResult>,
                response: Response<ThumbnailCropResult>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
//                        sendOk(it)
                    }
                } else {
                    sendFail()
                }
            }

            override fun onFailure(call: Call<ThumbnailCropResult>, t: Throwable) {
                resultReceiver.send(RESULT_CANCELED, Bundle().apply {
                    putSerializable(THROWABLE, t)
                })
            }
        })
    }

    private fun convertImageToBinary(file: File): MultipartBody.Part {
        return MultipartBody.Part.createFormData(
            "image",
            file.name,
            file.asRequestBody("application/octet-stream".toMediaTypeOrNull())
        )
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
}