package com.example.vision

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ResultReceiver
import com.example.vision.extension.convertImageToBinary
import com.example.vision.model.*
import com.example.vision.network.ApiFactory
import com.example.vision.network.VisionApi
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
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
     * @param lineBreak OCR을 통해 추출한 문장들의 줄바꿈 설정
     * @param callback 추출한 문자열 반환
     */
    fun getOcrResult(context: Context, lineBreak: Boolean = false, callback: (String) -> Unit) {
        selectImage(
            context,
            resultReceiver = resultReceiver(VisionApiOption(OCR, lineBreak), callback)
        )
    }

    private fun selectImage(
        context: Context,
        resultReceiver: ResultReceiver
    ) {
        context.startActivity(Intent(context, ImageSelectActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra(KEY_BUNDLE, Bundle().apply {
                putParcelable(IMAGE_SELECTOR, resultReceiver)
            })
        })
    }

    /**
     *
     * @param sentences 번역하고 싶은 문장
     * @param srcLang 번역하고 싶은 문장이 어떤 언어인지 명시
     * @param targetLang 어떤 언어로 번역된 결과를 얻고 싶은지 명시
     * @param callback 번역된 결과 반환
     */
    fun translateSentence(
        sentences: String,
        srcLang: Language,
        targetLang: Language,
        callback: (String) -> Unit
    ) {
        visionApi.translateSentence(sentences, srcLang.language, targetLang.language)
            .enqueue(object : Callback<TranslateResult> {
                override fun onResponse(
                    call: Call<TranslateResult>,
                    response: Response<TranslateResult>
                ) {
                    if (response.isSuccessful) {
                        // TODO: StringBuilder 확장함수 구현 및 관련 코드 리팩토링
                        val translateResult = StringBuilder()
                        response.body()?.translatedText?.get(0)?.forEach {
                            translateResult.append("$it ")
                        }
                        callback(translateResult.toString())
                    } else {
                        /*
                        TODO: 에러 상황 처리해야됨
                         1. 데이터는 전송했지만 번역이 실패한 경우
                         2. 네트워크 통신이 실패한 경우
                      */
                    }
                }

                override fun onFailure(call: Call<TranslateResult>, t: Throwable) {
                    // TODO 에러 상황 처리해야됨
                }
            })
    }

    fun getThumbnailImage(imageUrl: String, width: Int, height: Int, callback: (String) -> Unit) {
        visionApi.getThumbnailImage(imageUrl, width, height)
            .enqueue(object : Callback<ThumbnailCropResult> {
                override fun onResponse(
                    call: Call<ThumbnailCropResult>,
                    response: Response<ThumbnailCropResult>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            callback(it.thumbnailImageUrl)
                        }
                    } else {

                    }
                }

                override fun onFailure(call: Call<ThumbnailCropResult>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
    }

    fun getThumbnailImage(context: Context, width: Int, height: Int, callback: (String) -> Unit) {
        selectImage(
            context,
            resultReceiver(
                VisionApiOption(THUMBNAIL_CROP, width = width, height = height),
                callback
            )
        )
    }

    @JvmSynthetic
    internal fun resultReceiver(
        option: VisionApiOption,
        callback: (String) -> Unit
    ): ResultReceiver {
        return object : ResultReceiver(Handler(Looper.getMainLooper())) {
            override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
                if (resultCode == Activity.RESULT_OK) {
                    resultData?.let { data ->
                        val image =
                            data.getParcelable<ImageChooseResult>(IMAGE_CHOOSE_RESULT)?.image
                        image?.let {


                            when (option.api) {
                                OCR -> callOcrApi(it, option.lineBreak, callback)
                                THUMBNAIL_CROP -> callThumbnailCropApi(
                                    it,
                                    option.width,
                                    option.height,
                                    callback
                                )
                                THUMBNAIL_DETECT -> {
                                }
                                else -> {
                                    // Receive Fail
                                }
                            }
                        }
                    }
                } else {

                }
            }
        }
    }

    private fun callOcrApi(file: File, lineBreak: Boolean, callback: (String) -> Unit) {
        visionApi.getOcr(
            file.convertImageToBinary("image", "application/octet-stream")
        ).enqueue(object : Callback<OcrResult> {
            override fun onResponse(
                call: Call<OcrResult>,
                response: Response<OcrResult>
            ) {
                if (response.isSuccessful) {
                    val sb = StringBuilder()
                    response.body()?.let {
                        it.ocrDetailResult.forEach { ocr ->
                            sb.append(ocr.recognitionWords[0] + " ")
                        }
                        val sentences = sb.toString().run {
                            if (lineBreak) {
                                this.replace(".", ".\n").replace("?", "?\n")
                                    .replace("!", "!\n")
                            } else {
                                this
                            }
                        }
                        callback(sentences)
                    }
                }
            }

            override fun onFailure(call: Call<OcrResult>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun callThumbnailCropApi(
        file: File,
        width: Int,
        height: Int,
        callback: (String) -> Unit
    ) {
        val w = MultipartBody.Part.createFormData(WIDTH, width.toString())
        val h = MultipartBody.Part.createFormData(HEIGHT, height.toString())

        visionApi.getThumbnailImage(
            file.convertImageToBinary("image", "application/octet-stream"), w, h
        ).enqueue(object : Callback<ThumbnailCropResult> {
            override fun onResponse(
                call: Call<ThumbnailCropResult>,
                response: Response<ThumbnailCropResult>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback(it.thumbnailImageUrl)
                    }
                }
            }

            override fun onFailure(call: Call<ThumbnailCropResult>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    companion object {
        @JvmStatic
        val instance by lazy { VisionApiClient() }

        const val OCR = "ocr"
        const val THUMBNAIL_CROP = "thumbnail crop"
        const val THUMBNAIL_DETECT = "thumbnail detect"
    }
}
