package com.example.vision

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ResultReceiver
import android.util.Log
import com.example.vision.extension.convertImageToBinary
import com.example.vision.model.ImageChooseResult
import com.example.vision.model.VisionApiOption
import com.example.vision.model.VisionWrapper
import com.example.vision.model.face.FaceDetectResult
import com.example.vision.model.ocr.OcrResult
import com.example.vision.model.thumbnail.ThumbnailCropResult
import com.example.vision.model.thumbnail.ThumbnailDetectResult
import com.example.vision.model.translate.Language
import com.example.vision.model.translate.TranslateResult
import com.example.vision.network.ApiFactory
import com.example.vision.network.VisionApi
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.File

class VisionApiClient {
    private val visionApi by lazy {
        ApiFactory.vapi.create(VisionApi::class.java)
    }

    /**
     * OCR 기능 사용. 사진을 선택해 해당 사진에 있는 문자를 추출한다.
     *
     * @param context OCR 기능을 실행하기 위한 현재 Activity Context
     * @param callback 추출한 결과 반환
     */
    fun getOcrResult(context: Context, callback: (result: OcrResult?, error: Throwable?) -> Unit) {
        selectImage(
            context,
            resultReceiver = resultReceiver(
                VisionApiOption(OCR),
                callback as (VisionWrapper?, Throwable?) -> Unit
            )
        )
    }

    /**
     * 사용자가 원하는 언어로 문장 번역
     *
     * @param sentences 번역하고 싶은 문장
     * @param srcLang 번역하고 싶은 문장이 어떤 언어인지 명시 [Language]
     * @param targetLang 어떤 언어로 번역된 결과를 얻고 싶은지 명시 [Language]
     * @param callback 번역된 결과 반환
     */
    fun translateSentence(
        sentences: String,
        srcLang: Language,
        targetLang: Language,
        callback: (result: TranslateResult?, error: Throwable?) -> Unit
    ) {
        visionApi.translateSentence(sentences, srcLang.language, targetLang.language)
            .enqueue(object : Callback<TranslateResult> {
                override fun onResponse(
                    call: Call<TranslateResult>,
                    response: Response<TranslateResult>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            callback(it, null)
                            return
                        }
                        callback(null, RuntimeException("response body is empty"))
                    } else {
                        callback(null, HttpException(response))
                    }
                }

                override fun onFailure(call: Call<TranslateResult>, t: Throwable) {
                    callback(null, t)
                }
            })
    }

    /**
     * 웹 이미지로 썸네일 생성
     *
     * @param imageUrl 썸네일을 생성하고 싶은 이미지의 url
     * @param width 생성할 썸네일의 가로 길이
     * @param height 생성할 썸네일의 세로 길이
     * @param callback 썸네일 생성 결과 반환
     */
    fun createThumbnailImage(
        imageUrl: String,
        width: Int,
        height: Int,
        callback: (result: ThumbnailCropResult?, error: Throwable?) -> Unit
    ) {
        visionApi.createThumbnailImage(imageUrl, width, height)
            .enqueue(object : Callback<ThumbnailCropResult> {
                override fun onResponse(
                    call: Call<ThumbnailCropResult>,
                    response: Response<ThumbnailCropResult>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            callback(it, null)
                            return
                        }
                        callback(null, RuntimeException("response body is empty"))
                    } else {
                        callback(null, HttpException(response))
                    }
                }

                override fun onFailure(call: Call<ThumbnailCropResult>, t: Throwable) {
                    callback(null, t)
                }
            })
    }

    /**
     * 사용자의 디바이스에 있는 사진으로 썸네일 생성
     *
     * @param context 이미지 선택 Activity를 실행하기 위한 현재 Activity Context
     * @param width 생성할 썸네일의 가로 길이
     * @param height 생성할 썸네일의 세로 길이
     * @param callback 썸네일 생성 결과 반환
     */
    fun createThumbnailImage(
        context: Context,
        width: Int,
        height: Int,
        callback: (result: ThumbnailCropResult?, error: Throwable?) -> Unit
    ) {
        selectImage(
            context,
            resultReceiver(
                VisionApiOption(THUMBNAIL_CROP, width = width, height = height),
                callback as (VisionWrapper?, Throwable?) -> Unit
            )
        )
    }

    /**
     * 웹 이미지에서 썸네일 생성하기 적절한 부분을 검출
     *
     * @param imageUrl 검출하고 싶은 이미지의 url
     * @param width 생성하고 싶은 썸네일의 가로 길이
     * @param height 생성하고 싶은 썸네일의 세로 길이
     * @param callback 추천 결과 반환
     */
    fun detectThumbnailImage(
        imageUrl: String,
        width: Int,
        height: Int,
        callback: (result: ThumbnailDetectResult?, error: Throwable?) -> Unit
    ) {
        visionApi.detectThumbnailImage(imageUrl, width, height)
            .enqueue(object : Callback<ThumbnailDetectResult> {
                override fun onResponse(
                    call: Call<ThumbnailDetectResult>,
                    response: Response<ThumbnailDetectResult>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            callback(it, null)
                            return
                        }
                        callback(null, java.lang.RuntimeException("body is empty"))
                    }
                    callback(null, HttpException(response))
                }

                override fun onFailure(call: Call<ThumbnailDetectResult>, t: Throwable) {
                    callback(null, t)
                }
            })
    }

    /**
     * 사용자의 디바이스에 있는 사진으로 썸네일 생성하기 적절한 부분을 검출
     *
     * @param context 이미지 선택 Activity를 실행하기 위한 현재 Activity Context
     * @param width 생성하고 싶은 썸네일의 가로 길이
     * @param height 생성하고 싶은 썸네일의 세로 길이
     * @param callback 추천 결과 반환
     */
    fun detectThumbnailImage(
        context: Context,
        width: Int,
        height: Int,
        callback: (result: ThumbnailDetectResult?, error: Throwable?) -> Unit
    ) {
        selectImage(
            context,
            resultReceiver(
                VisionApiOption(THUMBNAIL_DETECT, width = width, height = height),
                callback as (VisionWrapper?, Throwable?) -> Unit
            )
        )
    }

    /**
     * 사용자의 디바이스에 있는 사진으로 사람 얼굴 검출
     *
     * @param context 이미지 선택 Activity를 실행하기 위한 현재 Activity Context
     * @param threshold 검출된 얼굴이 오검출인지를 판단하기 위해 사용하는 기준값. 0 ~ 1.0 사이의 값 (기본값 0.7)
     * @param callback 검출된 결과 반환
     */
    fun detectFace(
        context: Context,
        threshold: Float? = null,
        callback: (result: FaceDetectResult?, error: Throwable?) -> Unit
    ) {
        selectImage(
            context,
            resultReceiver(
                VisionApiOption(FACE_DETECT, threshold = threshold),
                callback as (VisionWrapper?, Throwable?) -> Unit
            )
        )
    }

    /**
     * 웹 이미지로 사람 얼굴 검출
     *
     * @param imageUrl 얼굴을 검출하고 싶은 이미지의 url
     * @param threshold 검출된 얼굴이 오검출인지를 판단하기 위해 사용하는 기준값. 0 ~ 1.0 사이의 값 (기본값 0.7)
     * @param callback 검출된 결과 반환
     */
    fun detectFace(
        imageUrl: String,
        threshold: Float? = null,
        callback: (result: FaceDetectResult?, error: Throwable?) -> Unit
    ) {
        val url = MultipartBody.Part.createFormData(IMAGE_URL, imageUrl)
        val th = MultipartBody.Part.createFormData(THRESHOLD, threshold.toString())
        visionApi.detectFace(url, th).enqueue(
            object : Callback<FaceDetectResult> {
                override fun onResponse(
                    call: Call<FaceDetectResult>,
                    response: Response<FaceDetectResult>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            callback(it, null)
                            return
                        }
                        callback(null, java.lang.RuntimeException("body is empty"))
                    } else {
                        callback(null, HttpException(response))
                    }
                }

                override fun onFailure(call: Call<FaceDetectResult>, t: Throwable) {
                    callback(null, t)
                }
            })
    }

    private fun selectImage(context: Context, resultReceiver: ResultReceiver) {
        context.startActivity(Intent(context, ImageSelectActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra(KEY_BUNDLE, Bundle().apply {
                putParcelable(IMAGE_SELECTOR, resultReceiver)
            })
        })
    }

    private fun callOcrApi(file: File, callback: (OcrResult?, Throwable?) -> Unit) {
        visionApi.getOcr(
            file.convertImageToBinary("image", "application/octet-stream")
        ).enqueue(object : Callback<OcrResult> {
            override fun onResponse(
                call: Call<OcrResult>,
                response: Response<OcrResult>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback(it, null)
                        return
                    }
                    callback(null, java.lang.RuntimeException("body is empty"))
                } else {
                    callback(null, HttpException(response))
                }
            }

            override fun onFailure(call: Call<OcrResult>, t: Throwable) {
                Log.d("Kakao Vision", "onFailure: $t")
                callback(null, t)
            }
        })
    }

    private fun callThumbnailCropApi(
        file: File,
        width: Int,
        height: Int,
        callback: (ThumbnailCropResult?, Throwable?) -> Unit
    ) {
        val w = MultipartBody.Part.createFormData(WIDTH, width.toString())
        val h = MultipartBody.Part.createFormData(HEIGHT, height.toString())

        visionApi.createThumbnailImage(
            file.convertImageToBinary("image", "application/octet-stream"), w, h
        ).enqueue(object : Callback<ThumbnailCropResult> {
            override fun onResponse(
                call: Call<ThumbnailCropResult>,
                response: Response<ThumbnailCropResult>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback(it, null)
                        return
                    }
                    callback(null, java.lang.RuntimeException("body is empty"))
                } else {
                    callback(null, HttpException(response))
                }
            }

            override fun onFailure(call: Call<ThumbnailCropResult>, t: Throwable) {
                callback(null, t)
            }
        })
    }

    private fun callThumbnailDetectApi(
        file: File,
        width: Int,
        height: Int,
        callback: (ThumbnailDetectResult?, Throwable?) -> Unit
    ) {
        val w = MultipartBody.Part.createFormData(WIDTH, width.toString())
        val h = MultipartBody.Part.createFormData(HEIGHT, height.toString())

        visionApi.detectThumbnailImage(
            file.convertImageToBinary("image", "application/octet-stream"), w, h
        ).enqueue(object : Callback<ThumbnailDetectResult> {
            override fun onResponse(
                call: Call<ThumbnailDetectResult>,
                response: Response<ThumbnailDetectResult>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback(it, null)
                        return
                    }
                    callback(null, java.lang.RuntimeException("body is empty"))
                } else {
                    callback(null, HttpException(response))
                }
            }

            override fun onFailure(call: Call<ThumbnailDetectResult>, t: Throwable) {
                callback(null, t)
            }
        })
    }

    private fun callFaceDetectApi(
        file: File,
        threshold: Float? = null,
        callback: (FaceDetectResult?, Throwable?) -> Unit
    ) {
        val th = MultipartBody.Part.createFormData(THRESHOLD, threshold.toString())
        visionApi.detectFace(
            file.convertImageToBinary("image", "application/octet-stream"), th
        ).enqueue(
            object : Callback<FaceDetectResult> {
                override fun onResponse(
                    call: Call<FaceDetectResult>,
                    response: Response<FaceDetectResult>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            callback(it, null)
                            return
                        }
                        callback(null, java.lang.RuntimeException("body is empty"))
                    } else {
                        callback(null, HttpException(response))
                    }
                }

                override fun onFailure(call: Call<FaceDetectResult>, t: Throwable) {
                    callback(null, t)
                }
            })
    }

    @JvmSynthetic
    internal fun resultReceiver(
        option: VisionApiOption,
        callback: (VisionWrapper?, Throwable?) -> Unit
    ): ResultReceiver {
        return object : ResultReceiver(Handler(Looper.getMainLooper())) {
            override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
                if (resultCode == Activity.RESULT_OK) {
                    resultData?.let { data ->
                        val image =
                            data.getParcelable<ImageChooseResult>(IMAGE_CHOOSE_RESULT)?.image
                        image?.let {
                            when (option.api) {
                                OCR -> callOcrApi(it, callback)
                                THUMBNAIL_CROP -> callThumbnailCropApi(
                                    it,
                                    option.width,
                                    option.height,
                                    callback
                                )
                                THUMBNAIL_DETECT -> callThumbnailDetectApi(
                                    image,
                                    option.width,
                                    option.height,
                                    callback
                                )
                                FACE_DETECT -> callFaceDetectApi(image, option.threshold, callback)
                                else -> callback(null, RuntimeException("Unknown Exception"))
                            }
                        }
                    }
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    val error = resultData?.getSerializable(EXCEPTION) as Throwable
                    callback(null, error)
                } else {
                    val error =
                        IllegalArgumentException("Unknown resultCode in VisionApiClient#onReceivedResult()")
                    callback(null, error)
                }
            }
        }
    }

    companion object {
        @JvmStatic
        val instance by lazy { VisionApiClient() }

        const val OCR = "ocr"
        const val THUMBNAIL_CROP = "thumbnail crop"
        const val THUMBNAIL_DETECT = "thumbnail detect"
        const val FACE_DETECT = "face detect"
    }
}
