package com.example.vision

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ResultReceiver
import com.example.vision.ImageSelectActivity.Companion.OCR_RESULT
import com.example.vision.model.OcrResult

class VisionApiClient {
    fun getOcrResult(context: Context, lineBreak: Boolean, callback: (String) -> Unit) {
        selectImage(context, lineBreak, callback)
    }

    private fun selectImage(context: Context, lineBreak: Boolean, callback: (String) -> Unit) {
        context.startActivity(Intent(context, ImageSelectActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra(IntentFactory.KEY_BUNDLE, Bundle().apply {
                putParcelable(IntentFactory.IMAGE_SELECTOR, resultReceiver(lineBreak, callback))
            })
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
                    // api 호출 결과 처리
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