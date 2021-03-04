package com.example.vision

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ResultReceiver
import android.util.Log
import com.example.vision.ImageSelectActivity.Companion.OCR_RESULT
import com.example.vision.model.OcrResult

class VisionApiClient {

    fun selectImage(context: Context, callback: () -> Unit) {
        context.startActivity(Intent(context, ImageSelectActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra(IntentFactory.KEY_BUNDLE, Bundle().apply {
                putParcelable(IntentFactory.IMAGE_SELECTOR, resultReceiver(callback))
            })
        })
    }

    @JvmSynthetic
    internal fun resultReceiver(callback: () -> Unit): ResultReceiver {
        return object : ResultReceiver(Handler(Looper.getMainLooper())) {
            override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
                if (resultCode == Activity.RESULT_OK) {
                    Log.d(
                        "Kakao Vision",
                        "onReceiveResult: ${resultData?.getParcelable<OcrResult>(OCR_RESULT)}"
                    )
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