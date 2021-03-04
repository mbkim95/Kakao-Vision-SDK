package com.example.vision

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ResultReceiver
import com.example.vision.model.Language
import com.example.vision.model.OcrResult
import com.example.vision.model.TranslateResult
import com.example.vision.network.ApiFactory
import com.example.vision.network.VisionApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VisionApiClient {
    private val visionApi by lazy {
        ApiFactory.vapi.create(VisionApi::class.java)
    }

    fun getOcrResult(context: Context, lineBreak: Boolean, callback: (String) -> Unit) {
        selectImage(context, lineBreak, callback)
    }

    private fun selectImage(context: Context, lineBreak: Boolean, callback: (String) -> Unit) {
        context.startActivity(Intent(context, ImageSelectActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra(KEY_BUNDLE, Bundle().apply {
                putParcelable(IMAGE_SELECTOR, resultReceiver(lineBreak, callback))
            })
        })
    }

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

    @JvmSynthetic
    internal fun resultReceiver(lineBreak: Boolean, callback: (String) -> Unit): ResultReceiver {
        return object : ResultReceiver(Handler(Looper.getMainLooper())) {
            override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
                if (resultCode == Activity.RESULT_OK) {
                    resultData?.let {
                        val result = it.getParcelable<OcrResult>(OCR_RESULT)
                        val sb = StringBuilder()
                        result?.ocrDetailResult?.forEach { ocr ->
                            sb.append(ocr.recognitionWords[0] + " ")
                        }
                        val sentences = sb.toString().run {
                            if (lineBreak) {
                                this.replace(".", ".\n").replace("?", "?\n").replace("!", "!\n")
                            } else {
                                this
                            }
                        }
                        callback(sentences)
                    }
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    /*
                     TODO 에러 상황 처리해야됨
                     1. 이미지는 전송했지만 413 뜨는 경우 (Payload Too Large)
                     2. 네트워크 통신이 실패한 경우
                     */
                }
            }
        }
    }

    companion object {
        @JvmStatic
        val instance by lazy { VisionApiClient() }
    }
}